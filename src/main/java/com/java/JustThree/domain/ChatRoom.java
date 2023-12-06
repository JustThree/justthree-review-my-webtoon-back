package com.java.JustThree.domain;

import com.java.JustThree.service.ChatService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;    // PK, AutoIncrement

    @ManyToOne(fetch = FetchType.LAZY)
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime created;

//    private Set<WebSocketSession> sessions = new HashSet<>();
    public void handleAction(WebSocketSession session, Chat message, ChatService service){
        //message 에 담긴 타입을 확인한다.
        //이때 message 에서 getType 으로 가져온 내용이
        //chatDto 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if(message.getType().equals(Chat.MessageType.ENTER)){
            //sessions 에 넘어온 session 을 담고,
//            sessions.add(session);

            //message 에는 입장하였다는 메시지를 띄워줍니다.
            message.setContents(message.getUsersNickname() + " 님이 입장하였습니다.");
            sendMessage(message,service);
        } else if (message.getType().equals(Chat.MessageType.TALK)) {
            message.setContents(message.getContents());
            sendMessage(message,service);
        }
    }
    public <T> void sendMessage(T message, ChatService service){
//        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions,message));
    }
}
