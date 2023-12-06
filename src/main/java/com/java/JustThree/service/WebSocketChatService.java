package com.java.JustThree.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.json.JSONObject;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@ServerEndpoint(value="/chat")
public class WebSocketChatService {

    private static Set<Session> clientSet =
            Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void onOpen(Session s) {
        if(!clientSet.contains(s)) {
            clientSet.add(s);
            s.setMaxIdleTimeout(60000);
            System.out.println("[open session] " + s);
        }else {
            System.out.println("the session already opened");
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{
        JSONObject jsonObject = new JSONObject(msg);
        // {"mid":"sdas","msg":"das","token":"thisistoken","date":"2023-12-05T08:35:02.615Z"}
        String usersNickname = jsonObject.getString("mid");
        String chatContents = jsonObject.getString("msg");
        LocalDateTime chatCreated = LocalDateTime.parse(jsonObject.getString("date").substring(0,19 ));
        System.out.println(usersNickname);
        System.out.println(chatContents);
        System.out.println(chatCreated);
        System.out.println("[receive message] " + msg);
        for(Session s : clientSet) {
            System.out.println("[send message] " + msg);
            s.getBasicRemote().sendText(msg);
        }
    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("[close session] " + s);
        try {
            s.close();
        } catch(Exception e) {}
        clientSet.remove(s);
    }
}
