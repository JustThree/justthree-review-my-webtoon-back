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
    private Integer users_id;

    @Enumerated(EnumType.ORDINAL)
    private RoleType users_role;

    @Column(nullable = false)
    private String users_nickname;

    @Column(nullable = false)
    private String users_pw;

    @Column(nullable = false, unique = true)
    private String users_email;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;

    @Builder.Default
    private String profile_url = "https://just-three.s3.ap-northeast-2.amazonaws.com/HDcat.png";

    @Builder.Default
    private int status_code = 1;

    public UsersDTO toDto() {
        return UsersDTO.builder()
                .users_id(users_id)
                .users_nickname(users_nickname)
                .users_email(users_email)
                .profile_url(profile_url)
                .status_code(status_code)
                .build();
    }
}
