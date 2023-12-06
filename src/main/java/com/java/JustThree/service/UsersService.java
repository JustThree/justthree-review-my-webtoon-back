package com.java.JustThree.service;

import com.java.JustThree.dto.JoinRequest;
import com.java.JustThree.dto.RoleType;
import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service

public class UsersService {

    private final UsersRepository ur;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


//    @Transactional(rollbackFor = Exception.class)
//    public Long insertUsers(JoinRequest joinDTO){
//        Users user = Users.builder()
//                .usersEmail(joinDTO.getUsersEmail())
//                .usersPw(bCryptPasswordEncoder.encode(joinDTO.getUsersPw()))
//                .usersNickname(joinDTO.getUsersNickname())
//                .usersRole(RoleType.USER)
//                .build();
//        return  ur.save(user).getUsersId();
//    }
}
