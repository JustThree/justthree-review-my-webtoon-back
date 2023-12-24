package com.java.JustThree.domain;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {

    private final Users users;

    public UserDetailsImpl(Users users) {
        this.users = users;
    }

    public List<String> getRoleList() {
        System.out.println(getClass().getName());
        if (!this.users.getUsersRole().isEmpty()) {
            System.out.println(getClass().getName()+ Arrays.asList(this.users.getUsersRole().split(",")));
            return Arrays.asList(this.users.getUsersRole().split(","));
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
        /*getRoleList().forEach(r -> {
            System.out.println("role : " + r);
            authorities.add(() -> {
                return r;
            });
        });*/
        System.out.println(authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.users.getUsersPw();
    }

    @Override
    public String getUsername() {
        return this.users.getUsersEmail();
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
