package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) //@CreatedDate 등 Auditing 기능 작동 목적
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long imgId;

    @ManyToOne
    @JoinColumn(name="board_id", referencedColumnName = "board_id")
    private Board board;

    @Column(name = "access_url")
    private String accessUrl; //s3 내부 이미지에 접근할 수 있는 URL

    @Column(name = "origin_name")
    private String originName; //이미지 파일 본래 이름

    @Column(name = "stored_name")
    private String storedName; //이미지 파일이 S3에 저장될 때 사용되는 이름

    public BoardImage(String originName){
        this.originName = originName;
        this.storedName = getFileName(originName);
        this.accessUrl = "";
    }
    public void setAccessUrl(String accessUrl){
        this.accessUrl = accessUrl;
    }

    // 이미지 파일의 확장자를 추출하는 메소드
    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public String getFileName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }
}
