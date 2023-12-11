package com.java.JustThree.service;

        import java.time.LocalDateTime;
        import java.util.*;

        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.java.JustThree.config.chat.ServerEndpointConfigurator;
        import com.java.JustThree.domain.Chat;
        import com.java.JustThree.dto.chat.ChatResponse;
        import jakarta.websocket.OnClose;
        import jakarta.websocket.OnMessage;
        import jakarta.websocket.OnOpen;
        import jakarta.websocket.Session;
        import jakarta.websocket.server.PathParam;
        import jakarta.websocket.server.ServerEndpoint;
//        import javax.websocket.OnClose;
//        import javax.websocket.OnMessage;
//        import javax.websocket.OnOpen;
//        import javax.websocket.Session;
//        import javax.websocket.server.PathParam;
//        import javax.websocket.server.ServerEndpoint;

        import lombok.extern.slf4j.Slf4j;
        import org.json.JSONObject;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

@Service
@Slf4j
@ServerEndpoint(value="/chat/{masterId}/users/{token}", configurator = ServerEndpointConfigurator.class) // masterId -> Webtoon의 masterId. 채팅방 구분
public class WebSocketService {

    // 현재 접속한 사람(세션) 리스트
    private static Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<Session>());

    // 접속한 사람(세션)을 방 별로 나누기 위해 MAP 제작
    private static Map<Long, ArrayList<Session>> sessionMap = Collections.synchronizedMap(new HashMap<Long, ArrayList<Session>>());

    // chatService => ChatRepository, ChatRoomRepository 와 연결된 Service
    private final ChatService chatService;
    @Autowired
    public WebSocketService(ChatService chatService){
        this.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session s, @PathParam("masterId") Long masterId, @PathParam("token") String token) {

        // ---------- 현재 접속자를 알기 위해 sessionMap과 clientSet에 session 대신 user의 닉네임 등을 넣을 예정 --------------
        // 현재 해당 웹툰 채팅방에 아무도 접속하지 않았을 경우. sessionMap 생성 후 사용자 추가.
        if(!sessionMap.containsKey(masterId)) {
            sessionMap.put(masterId, new ArrayList<>());
            sessionMap.get(masterId).add(s);
        }else{
            // 접속한 사람이 있을 경우 현재 사용자만 추가
            sessionMap.get(masterId).add(s);
        }

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
    public void onClose(Session s) {
        log.info("[close session] " + s);
        try {

            clientSet.remove(s);
            Long findRoom = -1L;
            for(Long key: sessionMap.keySet()){
                for(int j=0; j<sessionMap.get(key).size();j++){
                    if(sessionMap.get(key).get(j).equals(s)){
                        findRoom = key;
                    }
                }
            }
            sessionMap.get(findRoom).remove(s);
            s.close();
        } catch(Exception e) {}
        clientSet.remove(s);
    }
}