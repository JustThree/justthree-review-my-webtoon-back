package com.java.JustThree.domain;


import com.java.JustThree.dto.RoleType;
import com.java.JustThree.dto.UsersDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Entity

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer usersId;

    @Enumerated(EnumType.ORDINAL)
    private RoleType usersRole;

    @Column(nullable = false)
    private String usersNickname;

    @Column(nullable = false)
    private String usersPw;

    @Column(nullable = false, unique = true)
    private String usersEmail;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;

    @Builder.Default
    private String profileUrl = "https://just-three.s3.ap-northeast-2.amazonaws.com/HDcat.png";

    @Builder.Default
    private int statusCode = 1;

    public UsersDTO toDto() {
        return UsersDTO.builder()
                .usersId(usersId)
                .usersNickname(usersNickname)
                .usersEmail(usersEmail)
                .profileUrl(profileUrl)
                .statusCode(statusCode)
                .build();
    }
}
