package com.java.JustThree.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ChatListResponse {
    private String contents;
    private Long masterId;
    private String title;
    private LocalDateTime created;
    private String usersNickname;
    private String profileUrl;

}
