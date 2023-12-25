package com.java.JustThree.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.jwt.*;
import com.java.JustThree.repository.RefreshTokenRepository;
import com.java.JustThree.repository.UsersRepository;
import com.java.JustThree.service.UsersDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsersRepository usersRepository;
    private final UsersDetailService ud;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CorsConfig corsConfig;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf(AbstractHttpConfigurer :: disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer :: disable)
                .httpBasic(AbstractHttpConfigurer :: disable)
//                .addFilterBefore(jwtExceptionFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter())
                .authorizeHttpRequests(
                        authorize -> authorize
//                                .requestMatchers("/**").permitAll()
                                //공통으로 허용되는 주소
                                .requestMatchers(HttpMethod.GET,"/api/reset-password","/board/**","/board/notice",
                                        "/api/check-nickname","/api/check-nickname"
                                        ,"/chat/**","/chats/**","/ws/**","/api/webtoon/**","/mypage/userinfo/**","/mypage/follow/**","/mypage/reviewed/**","/mypage/interested/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/join","/api/verify-code","/api/logout","/api/email-verification").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/reset-password").permitAll()
                                //권한 처리하는 주소
                                .requestMatchers(HttpMethod.GET,"/api/auth/accessoken").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/webtoon/review/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE,"/board/**").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT,"/mypage/update").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/api/getUserList","/api/getUserList/**","/admin/**","/admin").hasAnyRole("ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManagerBean(), jwtProperties, refreshTokenRepository, jwtProvider, usersRepository);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), usersRepository, ud, jwtProperties, jwtProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() throws Exception{
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

}