package com.java.JustThree.dto;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class JoinRequest {
    private String usersEmail;
    private String usersNickname;
    private String usersPw;
    private String checkPw;
}
//회원가입용도
