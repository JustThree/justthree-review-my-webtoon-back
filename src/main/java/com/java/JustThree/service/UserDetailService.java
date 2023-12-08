package com.java.JustThree.service;

import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UsersRepository ur;

    @Override
    public Users loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        return ur.findByUsersEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다. : " + email));
    }
}