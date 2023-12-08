package com.java.JustThree.controller;

import com.java.JustThree.dto.chat.ChatInfoResponse;
import com.java.JustThree.dto.chat.ChatResponse;
import com.java.JustThree.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/list/{master_id}")
    public ResponseEntity<List<ChatResponse>> recentChatList(@PathVariable Long master_id){
        return ResponseEntity.ok(chatService.findChatInWebtoon(master_id));
    }

    @GetMapping("/info/{master_id}")
    public ResponseEntity<ChatInfoResponse> chatInfo(@PathVariable Long master_id){
        return ResponseEntity.ok(chatService.findChatInfo(master_id));
    }
}
