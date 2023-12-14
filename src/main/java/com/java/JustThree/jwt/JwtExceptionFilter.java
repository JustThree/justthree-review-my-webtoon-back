package com.java.JustThree.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter{

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, java.io.IOException {

        response.setCharacterEncoding("utf-8");
        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            log.info("ExpiredJwtException 발생함");
            Map<String, String> map = new HashMap<>();
            map.put("errortype", "Forbidden");
            map.put("code", "409");
            map.put("refresh","true");
            map.put("message", "만료된 토큰입니다. Refresh 토큰이 필요합니다.");
            log.error("만료된 토큰");

            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write(objectMapper.writeValueAsString(map));
        } catch (JwtException e){
            log.info("JwtException 발생함");
            Map<String, String> map = new HashMap<>();
            map.put("errortype", "Forbidden");
            map.put("code", "400");
            map.put("message", "변조된 토큰입니다. 로그인이 필요합니다.");
            log.error("변조된 토큰");
            response.getWriter().write(objectMapper.writeValueAsString(map));
            filterChain.doFilter(request, response);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}

