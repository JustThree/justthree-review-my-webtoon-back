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
    @Column(name = "users_id")
    private Integer usersId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "users_role")
    private RoleType usersRole;

    @Column(name = "users_nickname", nullable = false)
    private String usersNickname;

    @Column(name = "users_pw", nullable = false)
    private String usersPw;

    @Column(name = "users_email", nullable = false, unique = true)
    private String usersEmail;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;

    @Builder.Default
    @Column(name = "profile_url")
    private String profileUrl = "https://just-three.s3.ap-northeast-2.amazonaws.com/HDcat.png";

    @Builder.Default
    @Column(name = "status_code")
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
