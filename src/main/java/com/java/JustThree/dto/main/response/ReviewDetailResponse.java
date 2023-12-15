package com.java.JustThree.dto.main.response;


import com.java.JustThree.domain.Review;
import com.java.JustThree.domain.Star;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewDetailResponse {
    Long ReviewId;
    String webtoonTitle;
    String profileImg;
    String userNickName;
    Integer rating;
    String content;
    String webtoonImg;
    String genre;
    boolean checkLike;

    public static ReviewDetailResponse fromEntity(Review review, Star star, boolean checkLike){
        return ReviewDetailResponse.builder()
                .ReviewId(review.getReviewId())
                .webtoonTitle(review.getWebtoon().getTitle())
                .profileImg(review.getUsers().getProfileUrl())
                .userNickName(review.getUsers().getUsersNickname())
                .rating(star.getStarVal())
                .content(review.getContent())
                .webtoonImg(review.getWebtoon().getImageUrl())
                .genre(review.getWebtoon().getMainGenreCdNm())
                .checkLike(checkLike)
                .build();
    }
}
