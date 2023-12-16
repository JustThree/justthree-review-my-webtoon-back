package com.java.JustThree.dto.main.response;

import com.java.JustThree.domain.Review_Reply;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewReplyResponse {
    private String userNickname;
    private String content;
    public static ReviewReplyResponse fromEntity(Review_Reply reviewReply){
        return ReviewReplyResponse.builder()
                .userNickname(reviewReply.getUsers().getUsersNickname())
                .content(reviewReply.getContent())
                .build();
    }
}
