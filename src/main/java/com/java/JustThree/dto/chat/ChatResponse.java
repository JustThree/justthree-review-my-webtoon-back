package com.java.JustThree.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.JustThree.domain.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 접속한 채팅방의 과거 채팅 기록을 조회할 때 사용
@Getter
@ToString
public class ChatResponse {
    @JsonProperty
    private String contents;
    @JsonProperty
    private String created;
    @JsonProperty
    private String senderNickname;

    public ChatResponse(Chat chat, String senderNickname){
        this.contents = chat.getContents();
        this.created = chat.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.senderNickname = senderNickname;
    }
}
