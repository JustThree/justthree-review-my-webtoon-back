package com.java.JustThree.dto;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewedWebtoonResponse {
    private Long reviewId; //리뷰 아이디
    private String content; //리뷰 내용

    //유조
    private String usersNickname;//리뷰 작성자(유저) 닉네임.
    private String profileUrl;//리뷰 작성자(유저) 프사
    //웹툰
    private String title; //리뷰 웹툰 제목
    private String imageUrl;//리뷰 웹툰 사진
    private String pictrWritrNm;//그림작가
    private String sntncWritrNm;// 글작가
    //대댓글 개수
    private Long reviewReplyCount;//대댓글 개수
    //좋아요 개수
    private Long reviewHeartCount;//좋아요 개수

    public ReviewedWebtoonResponse(Users users, Webtoon webtoon, Long reviewId, String content,Long reviewReplyCount,Long reviewHeartCount){
        this.reviewId=reviewId;
        this.content=content;

        this.usersNickname=users.getUsersNickname();
        this.profileUrl=users.getProfileUrl();

        this.title=webtoon.getTitle();
        this.imageUrl=webtoon.getImageUrl();
        this.pictrWritrNm=webtoon.getPictrWritrNm();
        this.sntncWritrNm=webtoon.getSntncWritrNm();
        this.reviewReplyCount=reviewReplyCount;
        this.reviewHeartCount=reviewHeartCount;
    }
}
