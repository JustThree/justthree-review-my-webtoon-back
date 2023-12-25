package com.java.JustThree.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.JustThree.domain.Users;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatParticipantResponse {

    @JsonProperty
    private String usersId;
    @JsonProperty
    private String nickname;
    @JsonProperty
    private String profileUrl;

    public ChatParticipantResponse(Users users){
        this.usersId = String.valueOf(users.getUsersId());
        this.nickname = users.getUsersNickname();
        this.profileUrl = users.getProfileUrl();
    }

}
