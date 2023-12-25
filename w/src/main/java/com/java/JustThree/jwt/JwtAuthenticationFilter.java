package com.java.JustThree.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.domain.UserDetailsImpl;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.LoginRequest;
import com.java.JustThree.dto.Token;
import com.java.JustThree.repository.RefreshTokenRepository;
import com.java.JustThree.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/login", "POST");

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider, UsersRepository usersRepository) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter -- attemptAuthentication 진입");

        // Login request 정보 받기
        ObjectMapper om = new ObjectMapper();
        LoginRequest loginDTO = new LoginRequest();
        try {
            loginDTO = om.readValue(request.getInputStream(), LoginRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsersEmail(), loginDTO.getUsersPw());
       /*
       => AuthenticationManager 에게 인증 요청 (UserDetailsService 통해 DB에 존재하는 유저인지 확인)
       1. UserDetailsService 의 loadUserByUsername() 호출
       2. loadUserByUsername() 에서 리턴 받은 UserDetails 객체와 authenticationToken 의
          principal(사용자 입력 email), credentials(사용자 입력 password) 비교
       3. 비밀번호가 일치하면 Authentication 객체를 만들어서 필터체인으로 리턴, 일치하지 않으면 AuthenticationException 발생
       */

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("userEmail : " + userDetails.getUsers().getUsersEmail());
            log.info("userEmail : " + userDetails.getUsers().getUsersPw());
            log.info("========================================================");

        return authentication;
    }

    // authenticate 성공
    // 전달된 Authentication 객체를 통해 JWT Token 생성한 후 Response Header 에 담아 전송
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException {

        System.out.println("JwtAuthenticationFilter -- successfulAuthentication 진입");

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();



        String accessToken = jwtProvider.createAccessToken(userDetailsImpl.getUsers());
        String refreshToken = jwtProvider.createRefreshToken(userDetailsImpl.getUsers());

        Token jwtToken = Token
                .builder()
                .accessToken(jwtProperties.getTOKEN_PREFIX() + accessToken)
                .refreshToken(jwtProperties.getTOKEN_PREFIX() + refreshToken)
                .key(userDetailsImpl.getUsers().getUsersEmail())
                .build();
        try {
            jwtProvider.insertRefreshToken(userDetailsImpl.getUsers(), refreshToken);
        }catch (Exception e){
            jwtProvider.deleteRefreshToken(userDetailsImpl.getUsers());
            jwtProvider.insertRefreshToken(userDetailsImpl.getUsers(), refreshToken);
        }

        String jwtJson = new ObjectMapper().writeValueAsString(jwtToken);

        response.addHeader(jwtProperties.getHEADER_STRING(), jwtJson); // String 으로 만들어라
        setSuccessResponse(response, userDetailsImpl.getUsers());


    }

    // authenticate 성공 시 Response Body 에 담을 데이터
    private void setSuccessResponse(HttpServletResponse response, Users userDetails) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", userDetails.getUsersNickname());
        jsonObject.put("profileImg", userDetails.getProfileUrl());
        jsonObject.put("usersId", userDetails.getUsersId());
        jsonObject.put("usersRole", userDetails.getUsersRole());

        response.getWriter().print(jsonObject);
        response.getWriter().flush();
    }

    // authenticate 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException {
        System.out.println("JwtAuthenticationFilter -- unsuccessfulAuthentication 진입");

        String failedMessage = failed.getMessage();
        setFailureResponse(response, failedMessage);
    }

    // authenticate 실패 시 Response Body 에 담을 데이터
    private void setFailureResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);

        response.getWriter().print(jsonObject);
        response.getWriter().flush();
    }
}