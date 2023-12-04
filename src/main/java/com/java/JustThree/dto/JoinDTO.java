package com.java.JustThree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class JoinDTO {
    private String users_email;
    private String users_nickname;
    private String users_pw;
    private String check_pw;

}
