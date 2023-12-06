package com.java.JustThree.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
/**
 * 이걸 어디에 쓸까 고민중
 */
public class UsersResponse {
    private Long usersId;
    private String usersEmail;
    private String usersNickname;
    private String profileUrl;
    private LocalDateTime created;
    private int statusCode;
}
