package com.java.JustThree.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    public String usersEmail;
    public String usersPw;
}
//로그인 할때 사용
