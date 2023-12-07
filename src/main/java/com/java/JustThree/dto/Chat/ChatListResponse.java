package com.java.JustThree.dto.Chat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

// 접속한 채팅방의 과거 채팅 기록을 조회할 때 사용
@Getter
@Builder
@ToString
public class ChatListResponse {
    private String contents;
    private LocalDateTime created;
    private String senderNickname;
}
