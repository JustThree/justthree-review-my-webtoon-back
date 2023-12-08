package com.java.JustThree.jwt;

import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.Token;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtProvider {
    public String createAccessToken(Users userDetails, JwtProperties jwtProperties){
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsersEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getACCESS_TOKEN_EXPIRATION_TIME()))
                .claim("id", userDetails.getUsersId())
                .claim("nickname", userDetails.getUsersEmail())
                .signWith(jwtProperties.getKEY())
                .compact();
    }
    public String createRefreshToken(Users userDetails, JwtProperties jwtProperties){
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsersEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getREFRESH_TOKEN_EXPIRATION_TIME()))
                .claim("id", userDetails.getUsersId())
                .claim("nickname", userDetails.getUsersEmail())
                .signWith(jwtProperties.getKEY())
                .compact();
    }


}
