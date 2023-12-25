package com.java.JustThree.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

//        ErrorResponse errorResponse = new ErrorResponse("Expired JWT", "Your token has expired");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonError = objectMapper.writeValueAsString(errorResponse);
//        response.getWriter().write(jsonError);


    }
}
