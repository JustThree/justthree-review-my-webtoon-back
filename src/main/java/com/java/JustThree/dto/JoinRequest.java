package com.java.JustThree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class JoinRequest {
    private String usersEmail;
    private String usersNickname;
    private String usersPw;
    private String checkPw;
}
//회원가입용도
