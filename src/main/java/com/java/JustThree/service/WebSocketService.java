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
@ServerEndpoint(value="/chat", configurator = ServerEndpointConfigurator.class) // masterId -> Webtoon의 masterId. 채팅방 구분
public class WebSocketService {

    // 현재 채팅방 리스트를 조회중인 Session(사용자)
    private static Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<Session>());

    // 접속한 사람(세션)을 방 별로 나누기 위해 MAP 제작
    private static Map<Long, ArrayList<Session>> sessionMap = Collections.synchronizedMap(new HashMap<Long, ArrayList<Session>>());

    // chatService => ChatRepository, ChatRoomRepository 와 연결된 Service
    private final ChatService chatService;
    @Autowired
    private WebSocketService(ChatService chatService){
        this.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session s) throws IOException {

        // ---------- 현재 접속자를 알기 위해 sessionMap과 clientSet에 session 대신 user의 닉네임 등을 넣을 예정 --------------
//        Long masterId = Long.valueOf((String) s.getUserProperties().get("masterId"));
        // 현재 해당 웹툰 채팅방에 아무도 접속하지 않았을 경우. sessionMap 생성 후 사용자 추가.
        if(getTokenAndMasterId(s).length == 2 ){ // 채팅방 접속중
            Long masterId = Long.valueOf(getTokenAndMasterId(s)[1]);
            if(!sessionMap.containsKey(masterId)) {
                sessionMap.put(masterId, new ArrayList<>());
                sessionMap.get(masterId).add(s);
            }else{
                sessionMap.get(masterId).add(s);
            }
            sendCurrentParticipants(masterId);
        }else{  // 채팅방 리스트 조회중

            if(!clientSet.contains(s)){
                // 현재 접속자 명단에 추가
                clientSet.add(s);
                log.info("[open session] " + s);
                log.info("Chat Room List" + sessionMap);
            }else{
                log.info("the session already opened");
            }

        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{
        if (chatService == null) {
            log.error("ChatService is null");
            return;
        }
//        String token = (String) session.getUserProperties().get("token");

        String token = "123";
        // 채팅방에 접속했을 때
        Long masterId = Long.valueOf(getTokenAndMasterId(session)[1]);
        // 메시지 내용을 DB에 저장
        ChatResponse chat = chatService.save(msg, masterId, token);

        // DB 저장 성공 시
        if(chat!=null){
            // 같은 방에 있는 사람(세션)에게만 보낸다
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(chat);
            for(Session s : sessionMap.get(masterId)){
                log.info("[send message] " + chat);
                s.getBasicRemote().sendText(jsonString);
            }
        }

        for(Session s : clientSet){
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(chatService.findChatRoom(1));
            System.out.println(jsonString);
            s.getBasicRemote().sendText(jsonString);
        }

    }

    @OnClose
    public void onClose(Session s) {
        log.info("[close session] " + s);
        try {
//            Long masterId = Long.valueOf((String) s.getUserProperties().get("masterId"));
            if( getTokenAndMasterId(s).length == 2){
                Long masterId = Long.valueOf(getTokenAndMasterId(s)[1]);
                sessionMap.get(masterId).remove(s);
                if(!sessionMap.get(masterId).isEmpty()){
                    sendCurrentParticipants(masterId);
                }else{
                    sessionMap.remove(masterId);
                }
            }else{
                clientSet.remove(s);
            }

            s.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        clientSet.remove(s);
    }


    // 현재 인원수 조회
    private void sendCurrentParticipants(Long masterId) throws IOException {
        System.out.println(masterId);
        JSONObject currentParticipants = new JSONObject();
        int countParticipants = sessionMap.get(masterId).size();
        currentParticipants.put("currentParticipants", countParticipants);

        for(Session participant : sessionMap.get(masterId)){
            participant.getBasicRemote().sendText(String.valueOf(currentParticipants));
        }
    }

    private String[] getTokenAndMasterId(Session session){
        return session.getQueryString().split("%");
    }
    // 채팅방 리스트 조회 시, 현재 채팅 참여자가 있는 채팅방 조회
    public static Set<Long> getRoomsHavingCurrentPart(){
        return  sessionMap.isEmpty() ? null : sessionMap.keySet();
    }
}