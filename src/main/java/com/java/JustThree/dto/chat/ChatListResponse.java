package com.java.JustThree.dto.chat;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChatListResponse implements Comparable<ChatListResponse> {
    private String contents;
    private Long masterId;
    private String title;
    private String created;
    private String usersNickname;
    private String imageUrl;

//    public ChatListResponse(String contents, Long masterId, String title, LocalDateTime created, String usersNickname, String imageUrl){
//        this.contents = contents;
//        this.masterId = masterId;
//        this.title = title;
//        this.created = String.valueOf(created);
//        this.usersNickname = usersNickname;
//        this.imageUrl = imageUrl;
//    }
    public ChatListResponse(Chat chat, LocalDateTime created){
        Webtoon webtoon = chat.getWebtoon();
        Users users = chat.getUsers();
        this.contents = chat.getContents();
        this.masterId = webtoon.getMasterId();
        this.title = webtoon.getTitle();
        this.created = String.valueOf(created);
        this.usersNickname = users.getUsersNickname();
        this.imageUrl = webtoon.getImageUrl();
    }
    public ChatListResponse(Chat chat){
        Webtoon webtoon = chat.getWebtoon();
        Users users = chat.getUsers();
        this.contents = chat.getContents();
        this.masterId = webtoon.getMasterId();
        this.title = webtoon.getTitle();
        this.created = String.valueOf(chat.getCreated());
        this.usersNickname = users.getUsersNickname();
        this.imageUrl = webtoon.getImageUrl();
    }


    @Override
    public int compareTo(ChatListResponse chatListResponse) {
        LocalDateTime newDateTime = LocalDateTime.parse(chatListResponse.created);
        LocalDateTime oldDateTime = LocalDateTime.parse(created);
        if( newDateTime.isAfter(oldDateTime) ){
            return 1;
        }else if( newDateTime.isBefore(oldDateTime)){
            return -1;
        }

        return 0;
    }
}
