package com.java.JustThree.dto.main.response;


import com.java.JustThree.domain.Review;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WebtoonDetailReviewResponse {
    public String userNickName;
    public String content;
    public String imgUrl;
    public String reviewId;
    public int rating;
    public long heartCount;
    public long replyCount;
    public static WebtoonDetailReviewResponse fromEntity(Review review,int rating,long heartCount,long replyCount){
        return WebtoonDetailReviewResponse.builder()
                .userNickName(review.getUsers().getUsersNickname())
                .content(review.getContent())
                .imgUrl(review.getUsers().getProfileUrl())
                .reviewId(review.getReviewId().toString())
                .rating(rating)
                .heartCount(heartCount)
                .replyCount(replyCount)
                .build();
    }
}
