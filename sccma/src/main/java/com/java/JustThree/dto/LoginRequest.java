package com.java.JustThree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
    private String usersEmail;
    private String usersPw;
}
//로그인 할때 사용
