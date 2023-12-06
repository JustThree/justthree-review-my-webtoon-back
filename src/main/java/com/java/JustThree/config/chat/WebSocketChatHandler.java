package com.java.JustThree.config.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.domain.Chat;
import com.java.JustThree.service.WebSocketChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final WebSocketChatService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("payload : " + payload);

        Chat chatMessage = mapper.readValue(payload, Chat.class);
//        log.info();
    }
}
