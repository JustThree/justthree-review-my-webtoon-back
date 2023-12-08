package com.java.JustThree.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.JustThree.domain.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

// 접속한 채팅방의 과거 채팅 기록을 조회할 때 사용
@Getter
@ToString
public class ChatResponse {
    @JsonProperty
    private final String contents;
    @JsonProperty
    private final String created;
    @JsonProperty
    private final String senderNickname;

    public ChatResponse(Chat chat, String senderNickname){
        this.contents = chat.getContents();
        this.created = String.valueOf(chat.getCreated()).substring(0,19);
        this.senderNickname = senderNickname;
    }
}
