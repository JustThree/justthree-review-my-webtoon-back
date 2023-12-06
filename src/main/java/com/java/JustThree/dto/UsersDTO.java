package com.java.JustThree.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private Long usersId;
    private String usersNickname;
    private String usersEmail;
    private String profileUrl;
    private LocalDateTime created;
    private int statusCode;
}
