package com.java.JustThree.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
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
@ServerEndpoint(value="/chat", configurator = ServerEndpointConfigurator.class)
public class WebSocketService {


    // 채팅방 리스트를 조회하기 위해 접속한 clientSet(접속자의 Session 리스트)
    // 리스트를 조회중이라면 token만 존재하므로 getTokenAndMasterId(Session)의 길이는 1
    private static Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<Session>());

    // 채팅방은 웹툰의 masterId로 구분. 접속(소속)되어 있는 sessionMap(key = 웹툰의 masterId, value = 채팅방에 소속되어있는 접속자의 Session 리스트)
    // 접속한 사람(세션)을 방 별로 나누기 위해 MAP 제작
    private static Map<Long, ArrayList<Session>> sessionMap = Collections.synchronizedMap(new HashMap<Long, ArrayList<Session>>());

    // DB와 연결될 때는 chatService 사용
    private final ChatService chatService;
    @Autowired
    private WebSocketService(ChatService chatService){
        this.chatService = chatService;
    }


    @OnOpen
    public void onOpen(Session s) throws IOException {
        log.info("[open session] " + s);
        // Long masterId = Long.valueOf((String) s.getUserProperties().get("masterId"));
        if(isInChatRoom(s)){ // 채팅방 접속중

            Long masterId = getMasterId(s);

            // 현재 해당 웹툰 채팅방에 아무도 접속하지 않았을 경우. sessionMap 생성 후 사용자 추가.
            if(!sessionMap.containsKey(masterId)) {
                sessionMap.put(masterId, new ArrayList<>());
            }
            sessionMap.get(masterId).add(s);

            // 채팅방 인원 변경 처리
            sendNumOfCurrentParticipants(masterId);

        }else{  // 채팅방 리스트 조회중
            // 현재 리스트 조회 명단에 추가
            clientSet.add(s);
        }

        if(!clientSet.isEmpty()){
            sendUpdate();
        }

        log.info("Chat Room List" + sessionMap);
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{

        String token = getToken(session);
//        String token = "123";
        // 클라이언트에서 message가 오는 경우는 채팅을 보냈을 때 말고는 없음
        Long masterId = getMasterId(session);

        // 메시지 내용을 DB에 저장
        ChatResponse chat = chatService.save(msg, masterId, token);

        // DB 저장 성공 시
        if(chat!=null){

            // 클라이언트로 보낼 메세지를 json 형태의 String으로 변환
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(chat);
            log.info("[ send message to " + masterId + " ] " + jsonString);

            // 같은 방에 있는 사람(세션)에게만 보낸다
            for(Session s : sessionMap.get(masterId)){
                s.getBasicRemote().sendText(jsonString);
            }
        }

        sendUpdate();

    }

    @OnClose
    public void onClose(Session s) {
        log.info("[close session] " + s);
        try {
//            Long masterId = Long.valueOf((String) s.getUserProperties().get("masterId"));

            if(isInChatRoom(s)){
                Long masterId = getMasterId(s);

                // 해당 웹툰채팅방에서 close한 session을 remove
                sessionMap.get(masterId).remove(s);

                // 해당 웹툰채팅방에 사람이 없다면 sessionMap에서 웹툰 key를 삭제
                if(sessionMap.get(masterId).isEmpty()){
                    sessionMap.remove(masterId);
                }else{
                    // 사람이 있다면 웹툰 실시간 조회를 위해 메시지 보내기
                    sendNumOfCurrentParticipants(masterId);
                }
            }else{
                clientSet.remove(s);
            }


            sendUpdate();
            s.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        clientSet.remove(s);
    }


    // 현재 인원수 조회
    private void sendNumOfCurrentParticipants(Long masterId) throws IOException {
        System.out.println(masterId);
        JSONObject currentParticipants = new JSONObject();
        int countParticipants = sessionMap.get(masterId).size();
        currentParticipants.put("currentParticipants", countParticipants);

        for(Session participant : sessionMap.get(masterId)){
            participant.getBasicRemote().sendText(String.valueOf(currentParticipants));
        }
    }


    private boolean isInChatRoom(Session session){
        return getTokenAndMasterId(session).length == 2;
    }

    private String[] getTokenAndMasterId(Session session){
        return session.getQueryString().split("&");
    }

    private String getToken(Session session){
        return getTokenAndMasterId(session)[0].replace("%20", " ");
    }

    private Long getMasterId(Session session){
        return Long.valueOf(getTokenAndMasterId(session)[1] );
    }

    private void sendUpdate() throws IOException {
        if(!clientSet.isEmpty()){
            for(Session session : clientSet){
                session.getBasicRemote().sendText("update");
            }
        }
    }

    // 채팅방 리스트 조회 시, 현재 채팅 참여자가 있는 채팅방 조회
    public static Set<Long> getRoomsHavingCurrentPart(){
        return  sessionMap.isEmpty() ? null : sessionMap.keySet();
    }

}