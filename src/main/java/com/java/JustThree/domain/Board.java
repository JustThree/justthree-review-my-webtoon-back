package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) //@CreatedDate 등 Auditing 기능 작동 목적
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;
    private String title;
    private String content;
    private long viewCount;
    @Column(columnDefinition = "TIMESTAMP") //column 데이터 유형 timestamp 지정
    @Temporal(TemporalType.TIMESTAMP) //jpa에 해당 필드가 timestamp 유형으로 매핑됨을 알려줌
    @CreatedDate //spring data jpa에서 엔티티의 생성일자 관리하도록 지정
    private LocalDateTime created;
    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime updated;
    //@Column(columnDefinition = "TINYINT(1)")
    private boolean noticeYn;

}
