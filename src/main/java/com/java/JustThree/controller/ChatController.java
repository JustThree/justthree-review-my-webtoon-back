package com.java.JustThree.controller;

import com.java.JustThree.dto.chat.ChatResponse;
import com.java.JustThree.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> recentChatList(@RequestParam Long master_id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatInWebtoon(master_id));
    }
}
