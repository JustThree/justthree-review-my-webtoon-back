package com.java.JustThree.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ChatInfoResponse {
    // 제목
    private String title;
    // 장르
    private String genre;
    // 그림작가
    private String painter;
    // 글작가
    private String writer;
}
