package com.java.JustThree.jwt;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "my-jwt")
public class JwtProperties {

    private String secret;
    private String issuer;

    // 임시 키
    private SecretKey KEY = Jwts.SIG.HS256.key().build();

    private int ACCESS_TOKEN_EXPIRATION_TIME = 86400000; // 1일 (1/1000초)
    private int REFRESH_TOKEN_EXPIRATION_TIME = 86400000*7;
    private String TOKEN_PREFIX = "Bearer ";
    private String HEADER_STRING = "Authorization";
}
