package com.java.JustThree.jwt;

import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UsersRepository ur;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UsersRepository ur,
                                  JwtProperties jwtProperties,
                                  JwtProvider jwtProvider) {
        super(authenticationManager);
        this.ur = ur;
        this.jwtProperties = jwtProperties;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // 헤더의 JWT 검증 -> 헤더에 JWT가 없으면 인증이 필요 없는 요청이므로 바로 filterChain 리턴
        String header = request.getHeader(jwtProperties.getHEADER_STRING());

//        log.info(header);
        //헤더가 없거나, Authorization이 없거나, X-Refresh-Token가 ture이면 패스
        if (header == null || !header.startsWith(jwtProperties.getTOKEN_PREFIX()) || Objects.equals(request.getHeader("X-Refresh-Token"), "true")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("헤더가 존재합니다");
        // Bearer 다음에 시작하는 문자열이 토큰이므로 'Bearer + 공백'을 제거한 문자열 추출
        String token = header.replace(jwtProperties.getTOKEN_PREFIX(), "");

        log.info(jwtProperties.getSecretKey()+"");
        // 토큰 검증 및 사용자 이메일 정보 추출 (토큰 검증에 실패하면 예외 발생)
//        String userEmail = Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();

        jwtProvider.validateAceessToken(token);

        String userEmail = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (userEmail != null) {
            Users user = ur.findByUsersEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("JwtAuthorizationFilter : 해당 email을 찾을 수 없습니다."));

            // 권한 처리를 위해 Authentication 객체를 생성한 후 SecurityContext 에 저장
            // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new org.springframework.security.core.userdetails.User(user.getUsersEmail(), "", user.getAuthorities()),
                    null, // 이미 검증이 끝났으므로 비밀번호는 필요 없음
                    user.getAuthorities() // 권한 정보
            );

            // 강제로 Security 세션에 접근하여 Authentication 객체 저장
            // SecurityContext 에 Authentication 객체를 저장하면, 권한이 필요한 페이지에 접근할 때 SecurityContextHolder 에서 권한 정보를 참조함
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}