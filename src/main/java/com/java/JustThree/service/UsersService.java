package com.java.JustThree.service;

import com.java.JustThree.dto.JoinDTO;
import com.java.JustThree.dto.UsersDTO;
import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class UsersService {

    private final UsersRepository ur;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UsersDTO insertUsers(JoinDTO joinDTO){
        Users user = Users.builder()
                .users_email(joinDTO.getUsers_email())
                .users_pw(bCryptPasswordEncoder.encode(joinDTO.getUsers_pw()))
                .users_nickname(joinDTO.getUsers_nickname())
                .build();

        ur.save(user);
        return user.toDto();
    }
}
