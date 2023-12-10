package com.java.JustThree.service;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.dto.chat.ChatInfoResponse;
import com.java.JustThree.dto.chat.ChatListResponse;
import com.java.JustThree.dto.chat.ChatResponse;
import com.java.JustThree.repository.ChatRepository;
import com.java.JustThree.repository.UsersRepository;
import com.java.JustThree.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*",exposedHeaders = "Authorization", allowCredentials = "true")
public class ChatService {

    private final WebtoonRepository webtoonRepository;
    private final UsersRepository usersRepository;
    private final ChatRepository chatRepository;

    public ChatResponse save(String msg, Long masterId, String token){
        Chat chat = Chat.builder()
                .contents(msg)
                .webtoon(webtoonRepository.findById(masterId).get())
                .users(usersRepository.findById(1L).get()) //  findById(jwtService.getId(token))
                .created(LocalDateTime.now())
                .build();
        try{
            chatRepository.save(chat);
            return new ChatResponse(chat, chat.getUsers().getUsersNickname());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<ChatResponse> findChatInWebtoon(Long masterId){
        List<ChatResponse> response = new ArrayList<>();
        chatRepository.findByWebtoon_mastrIdOrderByCreated(masterId)
                .forEach(element -> response.add(
                        new ChatResponse(element, element.getUsers().getUsersNickname())
                ));
        return response;
    }

    public ChatInfoResponse findChatInfo(Long master_id){
        Webtoon webtoon = webtoonRepository.findById(master_id)
                .orElseThrow(() -> new IllegalArgumentException());


        return ChatInfoResponse.builder()
                .title(webtoon.getTitle())
                .genre(webtoon.getMainGenreCdNm())
                .writer(webtoon.getSntncWritrNm())
                .painter(webtoon.getPictrWritrNm())
                .build();
    }

    public List<ChatListResponse> findChatRoom(int page){
//         [page] 1: all, 2:current, 3: hotWebtoon, 4: my
        switch (page){
            case 1:
                return chatRepository.findLatestChats();
            case 2:
                Set<Long> currentChat = WebSocketService.getRoomsHavingCurrentPart();

                if(currentChat == null){
                    return new ArrayList<ChatListResponse>();
                }else{

                    return chatRepository.findLatestChats()
                            .stream().filter(c -> currentChat.stream()
                                    .anyMatch(Predicate.isEqual(c.getMasterId()))).collect(Collectors.toList());
                }
            default:
                return null;
        }
    }
    public List<ChatListResponse> findChatRoomByUserId(Long users_id){

        return null;
    }


}
