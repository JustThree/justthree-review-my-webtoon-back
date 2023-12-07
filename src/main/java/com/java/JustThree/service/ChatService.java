package com.java.JustThree.service;

        import java.time.LocalDateTime;
        import java.util.*;

        import com.java.JustThree.domain.Chat;
        import com.java.JustThree.repository.ChatRepository;
        import jakarta.websocket.OnClose;
        import jakarta.websocket.OnMessage;
        import jakarta.websocket.OnOpen;
        import jakarta.websocket.Session;
        import jakarta.websocket.server.PathParam;
        import jakarta.websocket.server.ServerEndpoint;

        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.json.JSONObject;
        import org.springframework.stereotype.Service;

@Service
@Slf4j
@ServerEndpoint(value="/chat/{masterId}") // masterId -> Webtoon의 masterId. 채팅방 구분
@RequiredArgsConstructor
public class ChatService {

    // 현재 접속한 사람(세션) 리스트
    private static final Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<Session>());

    private final ChatRepository chatRepository;

    // 접속한 사람(세션)을 방 별로 나누기 위해 MAP 제작
    private static Map<Long, ArrayList<Session>> sessionMap = Collections.synchronizedMap(new HashMap<Long, ArrayList<Session>>());

    @OnOpen
    public void onOpen(Session s, @PathParam("masterId") Long masterId) {

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
            System.out.println(sessionMap);
        }else{
            log.info("the session already opened");
        }

    }

    @OnMessage
    public void onMessage(String msg, Session session, @PathParam("masterId") Long masterId) throws Exception{

        // msg 예시: {"sender":"애리밍","contents":"하이티비","created":"2023-12-07T00:30:08.205Z"}
        // **  token 추가 예정 **
        JSONObject jsonObject = new JSONObject(msg);
        String usersNickname = jsonObject.getString("sender");  // 메세지를 보낸 사용자의 닉네임
        String chatContents = jsonObject.getString("contents"); // 메세지 내용
        // 2023-12-07T00:30:08.205Z 에서 date를 초단위까지 자르기
//        LocalDateTime chatCreated = LocalDateTime.parse(jsonObject.getString("created").substring(0,19 ));

        try{
            // Users users = usersRepository.findById(jwtService.getId(token));
//            chatRepository.save()
        }catch (Exception e){
            e.printStackTrace();
        }
        // ----- 현재 사용자가 접속한 방을 PathParam으로 찾는 것으로 변경-----
        /*
        // 사용자가 어느 방에 있는 지 찾기.
        Long findRoom = -1L;
        // sessionMap 예시, {1=[org.apache.tomcat.websocket.WsSession@2c439054]}
        // sessionMap의 key -> 현재 열려있는 채팅방의 번호
        for(Long key : sessionMap.keySet()){
            // 체
            for(int  j = 0 ; j < sessionMap.get(key).size(); j++){
                if(sessionMap.get(key).get(j).equals(session)){
                    findRoom = key;
                }
            }
        }
        */

            // 같은 방에 있는 사람(세션)에게만 보낸다
            for(Session s : sessionMap.get(masterId)){
                System.out.println("[send message] " + msg);
                s.getBasicRemote().sendText(msg);
            }


    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("[close session] " + s);
        System.out.println(sessionMap);
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
            System.out.println(sessionMap);
        } catch(Exception e) {}
        clientSet.remove(s);
    }
}