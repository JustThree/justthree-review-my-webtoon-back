package com.java.JustThree.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.config.chat.ServerEndpointConfigurator;
import com.java.JustThree.dto.chat.ChatResponse;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ServerEndpoint(value="/chat/{masterId}/users/{token}", configurator = ServerEndpointConfigurator.class) // masterId -> Webtoon의 masterId. 채팅방 구분
public class WebSocketService {

    // 현재 접속한 사람(세션) 리스트
    public static Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<Session>());

    // 접속한 사람(세션)을 방 별로 나누기 위해 MAP 제작
    private static Map<Long, ArrayList<Session>> sessionMap = Collections.synchronizedMap(new HashMap<Long, ArrayList<Session>>());

    // chatService => ChatRepository, ChatRoomRepository 와 연결된 Service
    private final ChatService chatService;
    @Autowired
    private WebSocketService(ChatService chatService){
        this.chatService = chatService;
    }

    // 현재 인원수 조회
    private void sendCurrentParticipants(Long masterId) throws IOException {
        JSONObject currentParticipants = new JSONObject();
        int countParticipants = sessionMap.get(masterId).size();
        currentParticipants.put("currentParticipants", countParticipants);
        for(Session participant : sessionMap.get(masterId)){
            participant.getBasicRemote().sendText(String.valueOf(currentParticipants));
        }
    }

    @OnOpen
    public void onOpen(Session s, @PathParam("masterId") Long masterId, @PathParam("token") String token) throws IOException {

        // ---------- 현재 접속자를 알기 위해 sessionMap과 clientSet에 session 대신 user의 닉네임 등을 넣을 예정 --------------

        // 현재 해당 웹툰 채팅방에 아무도 접속하지 않았을 경우. sessionMap 생성 후 사용자 추가.
        if(!sessionMap.containsKey(masterId)) {
            sessionMap.put(masterId, new ArrayList<>());
            sessionMap.get(masterId).add(s);
        }else{
            sessionMap.get(masterId).add(s);
        }

        sendCurrentParticipants(masterId);

        if(!clientSet.contains(s)){
            // 현재 접속자 명단에 추가
            clientSet.add(s);
            log.info("[open session] " + s);
            log.info("Chat Room List" + sessionMap);
        }else{
            log.info("the session already opened");
        }

    }

    @OnMessage
    public void onMessage(String msg, Session session, @PathParam("masterId") Long masterId,@PathParam("token") String token) throws Exception{
        if (chatService == null) {
            log.error("ChatService is null");
            return;
        }

        // 메시지 내용을 DB에 저장
        ChatResponse chat = chatService.save(msg, masterId, token);

        // DB 저장 성공 시
            if(chat!=null){
                // 같은 방에 있는 사람(세션)에게만 보낸다
                for(Session s : sessionMap.get(masterId)){
                    log.info("[send message] " + chat);
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println(chat);
                    String jsonString = mapper.writeValueAsString(chat);
                    s.getBasicRemote().sendText(jsonString);
                }
            }

    }

    @OnClose
    public void onClose(Session s, @PathParam("masterId") Long masterId) {
        log.info("[close session] " + s);
        try {
            clientSet.remove(s);
            sessionMap.get(masterId).remove(s);
            if(!sessionMap.get(masterId).isEmpty()){
                sendCurrentParticipants(masterId);
            }

            s.close();
        } catch(Exception e) {}
        clientSet.remove(s);
    }
}