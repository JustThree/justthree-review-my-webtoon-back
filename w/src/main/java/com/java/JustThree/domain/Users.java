package com.java.JustThree.domain;


import com.java.JustThree.dto.UsersResponse;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Entity
@Setter
public class Users {
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

    public static UsersResponse toDto(Users users) {
        return UsersResponse.builder()
                .usersId(users.usersId)
                .usersNickname(users.usersNickname)
                .usersEmail(users.usersEmail)
                .created(users.created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .profileUrl(users.profileUrl)
                .statusCode(users.statusCode)
                .build();
    }

/*    public List<String> getRoleList() {
        System.out.println(getClass().getName());
        if (!this.usersRole.isEmpty()) {
            System.out.println(getClass().getName()+Arrays.asList(this.usersRole.split(",")));
            return Arrays.asList(this.usersRole.split(","));
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<String> roles = getRoleList();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        }
        *//*getRoleList().forEach(r -> {
            System.out.println("role : " + r);
            authorities.add(() -> {
                return r;
            });
        });*//*
        System.out.println(authorities);
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
    }*/

    public void changePassword(String password) {
        this.usersPw = password;
    }

    public void disableUser() {this.statusCode = 0;}

    public void updateUser(String nickname, String profileUrl) {
        this.usersNickname=nickname;
        this.profileUrl=profileUrl;
    }
}
