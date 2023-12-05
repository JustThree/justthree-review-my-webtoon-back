package com.java.JustThree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class JoinDTO {
    private String usersEmail;
    private String usersNickname;
    private String usersPw;
    private String checkPw;

}
