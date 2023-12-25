package com.java.JustThree.jwt;

import com.java.JustThree.domain.RefreshToken;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.Token;
import com.java.JustThree.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createAccessToken(Users userDetails){
        log.info("create AccessToken");
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")  // 헤더 설정
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(userDetails.getUsersEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getACCESS_TOKEN_EXPIRATION_TIME()))
                .claim("id", userDetails.getUsersId())
                .signWith(jwtProperties.getSecretKey())  // 암호화 알고리즘과 키 설정
                .compact();
    }
    public String createRefreshToken(Users userDetails){
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")  // 헤더 설정
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(userDetails.getUsersEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getREFRESH_TOKEN_EXPIRATION_TIME()))
                .claim("id", userDetails.getUsersId())
                .signWith(jwtProperties.getSecretKey())
                .compact();
    }

    public void insertRefreshToken(Users user, String refreshToken){
        RefreshToken refreshToken1 = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(refreshToken1);
    }

    @Transactional
    public void deleteRefreshToken(Users user){
        refreshTokenRepository.deleteByUser_UsersEmail(user.getUsersEmail());
    }
    public boolean validateAceessToken(String token){

        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecretKey()).build().parseClaimsJws(token);

        return claims.getBody().getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public Long getUserId(String token){

        String accessToken = token.replace(jwtProperties.getTOKEN_PREFIX(), "");
        try {
            Jws<Claims> jws = Jwts.parserBuilder()  // This line will change depending on the version of jjwt you're using
                    .setSigningKey(jwtProperties.getSecretKey())  // Replace with signing key
                    .build()
                    .parseClaimsJws(accessToken);
            return Long.parseLong(jws.getBody().get("id").toString());
        }catch (JwtException e){
            return null;
        }



    }

    public String getUserEmail(String token){
        String accessToken = token.replace(jwtProperties.getTOKEN_PREFIX(), "");

        Jws<Claims> jws = Jwts.parserBuilder()  // This line will change depending on the version of jjwt you're using
                .setSigningKey(jwtProperties.getSecretKey())  // Replace with signing key
                .build()
                .parseClaimsJws(accessToken);

        return jws.getBody().getSubject();
    }



}
