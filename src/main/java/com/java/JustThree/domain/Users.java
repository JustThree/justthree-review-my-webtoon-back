package com.java.JustThree.domain;


import com.java.JustThree.dto.RoleType;
import com.java.JustThree.dto.UsersResponse;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Entity
@Data
@Setter

public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long usersId;

    @Column(name = "users_role")
    private String usersRole;

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

    public UsersResponse toDto() {
        return UsersResponse.builder()
                .usersId(usersId)
                .usersNickname(usersNickname)
                .usersEmail(usersEmail)
                .profileUrl(profileUrl)
                .statusCode(statusCode)
                .build();
    }

    public List<String> getRoleList() {
        if (!this.usersRole.isEmpty()) {
            return Arrays.asList(this.usersRole.split(","));
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        getRoleList().forEach(r -> {
            System.out.println("role : " + r);
            authorities.add(() -> {
                return r;
            });
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return usersPw;
    }

    @Override
    public String getUsername() {
        return usersEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
