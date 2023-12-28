package com.java.JustThree.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.domain.UserDetailsImpl;
import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import com.java.JustThree.service.UsersDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

@Slf4j

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UsersRepository ur;
    private final UsersDetailService ud;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UsersRepository ur,
                                  UsersDetailService ud, JwtProperties jwtProperties,
                                  JwtProvider jwtProvider) {
        super(authenticationManager);
        this.ur = ur;
        this.ud = ud;
        this.jwtProperties = jwtProperties;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("1. 권한이나 인증이 필요한 요청이 전달됨");
        // 헤더의 JWT 검증 -> 헤더에 JWT가 없으면 인증이 필요 없는 요청이므로 바로 filterChain 리턴
        String header = request.getHeader(jwtProperties.getHEADER_STRING());

        //헤더가 없거나, Authorization이 없거나, X-Refresh-Token가 ture이면 패스
        if (header == null || !header.startsWith(jwtProperties.getTOKEN_PREFIX()) || Objects.equals(request.getHeader("X-Refresh-Token"), "true")) {
            filterChain.doFilter(request, response);

            return;
        }
        // Bearer 다음에 시작하는 문자열이 토큰이므로 'Bearer + 공백'을 제거한 문자열 추출
        String token = header.replace(jwtProperties.getTOKEN_PREFIX(), "");

        log.info(jwtProperties.getSecretKey()+"");

        try {
            jwtProvider.validateAceessToken(token);

        }catch (JwtException e){
//            ObjectMapper mapper = new ObjectMapper();
//
//            ResponseStatusException responseStatusException = new ResponseStatusException(
//                    HttpStatus.NOT_ACCEPTABLE, "토큰이 만료되었습니다.");
//
//            mapper.writeValue(response.getWriter(), responseStatusException);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        // 토큰 검증 및 사용자 이메일 정보 추출 (토큰 검증에 실패하면 예외 발생)
        String userEmail = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if (userEmail != null) {
            Users user = ur.findByUsersEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("JwtAuthorizationFilter : 해당 email을 찾을 수 없습니다."));
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            // 권한 처리를 위해 Authentication 객체를 생성한 후 SecurityContext 에 저장
            // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            // 강제로 Security 세션에 접근하여 Authentication 객체 저장
//             SecurityContext 에 Authentication 객체를 저장하면, 권한이 필요한 페이지에 접근할 때 SecurityContextHolder 에서 권한 정보를 참조함
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}