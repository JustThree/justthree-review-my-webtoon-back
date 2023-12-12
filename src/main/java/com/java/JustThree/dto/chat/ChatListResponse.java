package com.java.JustThree.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChatListResponse {
    private String contents;
    private Long masterId;
    private String title;
    private String created;
    private String usersNickname;
    private String imageUrl;

    public ChatListResponse(String contents, Long masterId, String title, LocalDateTime created, String usersNickname, String imageUrl){
        this.contents = contents;
        this.masterId = masterId;
        this.title = title;
        this.created = String.valueOf(created);
        this.usersNickname = usersNickname;
        this.imageUrl = imageUrl;
    }
}
