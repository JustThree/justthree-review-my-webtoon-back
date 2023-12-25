package com.java.JustThree.dto.main.response;

import com.java.JustThree.domain.Review_Reply;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewReplyResponse {
    private Long replyId;
    private Long replyUserId;
    private String userNickname;
    private String content;
    private String imgUrl;
    public static ReviewReplyResponse fromEntity(Review_Reply reviewReply){
        return ReviewReplyResponse.builder()
                .replyId(reviewReply.getReviewReplyId())
                .replyUserId(reviewReply.getUsers().getUsersId())
                .userNickname(reviewReply.getUsers().getUsersNickname())
                .content(reviewReply.getContent())
                .imgUrl(reviewReply.getUsers().getProfileUrl())
                .build();
    }
}
