package com.java.JustThree.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.JustThree.domain.ChatRoom;
import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.repository.ChatRoomRepository;
import com.java.JustThree.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Service
public class ChatService {
    private final ObjectMapper mapper;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;

//    public List<ChatRoom> findAllRoom() {
//        return new ArrayList<>(chatRoomRepository.findAllById());
//    }

    public List<ChatRoom> findTop5ByUser(Long users_id){
        Users users = usersRepository.findById(users_id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + users_id));
        return chatRoomRepository.findTop5ByUsers(users);
    }


//    public ChatRoom createRoom(Webtoon webtoon) {}
}
