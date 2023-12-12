package com.java.JustThree.service;

import com.java.JustThree.domain.RefreshToken;
import com.java.JustThree.dto.JoinRequest;
import com.java.JustThree.dto.RoleType;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.UsersResponse;
import com.java.JustThree.jwt.JwtProperties;
import com.java.JustThree.jwt.JwtProvider;
import com.java.JustThree.repository.RefreshTokenRepository;
import com.java.JustThree.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j

public class UsersService {

    private final UsersRepository ur;
    private final RefreshTokenRepository rtr;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;


    @Transactional(rollbackFor = Exception.class)
    public Long insertUsers(JoinRequest joinDTO){
        Users user = Users.builder()
            .usersEmail(joinDTO.getUsersEmail())
            .usersPw(bCryptPasswordEncoder.encode(joinDTO.getUsersPw()))
            .usersNickname(joinDTO.getUsersNickname())
            .usersRole(RoleType.USER.name())
            .build();
        return  ur.save(user).getUsersId();
    }

    public UsersResponse getUserInfo(String token){

        Users user = ur.findById(jwtProvider.getUserId(token)).get();

        return user.toDto();
    }

    @Transactional
    public void logout(String usersEmail) {
        rtr.deleteByUser_UsersEmail(usersEmail);
    }

    public String something(String token){

            log.info("아이디가 도착했습니다");
            log.info(jwtProvider.getUserId(token)+"");

        return "";
    }

    public String getNewAccessToken(String token){
        String userEmail = jwtProvider.getUserEmail(token);
        RefreshToken refreshToken = rtr.findByUser_UsersEmail(userEmail);
        return refreshToken.getRefreshToken();
    }
}
