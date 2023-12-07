package com.java.JustThree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateUserRequest {
    private String usersId;
    private String usersNickname;
    private String usersPw;
    private String profileUrl;
}
