package com.java.JustThree.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.jwt.*;
import com.java.JustThree.repository.RefreshTokenRepository;
import com.java.JustThree.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter(), JwtAuthorizationFilter.class)

                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        System.out.println("등록");
        return new JwtAuthenticationFilter(authenticationManagerBean(), jwtProperties, refreshTokenRepository, jwtProvider, usersRepository);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), usersRepository, jwtProperties, jwtProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() throws Exception{
        return new JwtExceptionFilter(objectMapper);
    }

}