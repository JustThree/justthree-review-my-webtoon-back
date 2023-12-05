package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@EntityListeners(AuditingEntityListener.class) //@CreatedDate 등 Auditing 기능 작동 목적
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private long imgId;
    @ManyToOne
    @JoinColumn(name="board_id", referencedColumnName = "board_id")
    private Board board;
    @Column(name = "access_url")
    private String accessUrl;
    @Column(name = "origin_name")
    private String originName;
    @Column(name = "stored_name")
    private String storedName;
}
