package com.java.JustThree.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "my-jwt")
public class JwtProperties {

    private String secret;
    private String issuer;

    // 임시 키
    private Key key =  Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private int ACCESS_TOKEN_EXPIRATION_TIME = 86400000; // 86400000 1일 (1/1000초)
    private int REFRESH_TOKEN_EXPIRATION_TIME = 86400000*7;// *7;

    private String TOKEN_PREFIX = "Bearer ";
    private String HEADER_STRING = "Authorization";

    public Key getSecretKey(){
        byte[] keyBytes = DigestUtils.sha256(this.secret);  // Create 256-bit hash
        Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
        return key;
    }
}
