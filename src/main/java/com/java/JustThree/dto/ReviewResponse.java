package com.java.JustThree.dto;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewId; //리뷰 아이디
    private String content; //리뷰 내용

    //유조
    private String usersNickname;//리뷰작성자 닉네임.
    private String profileUrl;//리뷰 작성자 프사
    //웹툰
    private String title; //리뷰 웹툰 제목
    private String imageUrl;//리뷰 웹툰 사진
    private String pictrWritrNm;//그림작가
    private String sntncWritrNm;// 글작가
    //스타
    private int starVal;//별점

    public ReviewResponse(Users users, Webtoon webtoon,Long reviewId,int starVal,String content){
        this.reviewId=reviewId;
        this.content=content;

        this.usersNickname=users.getUsersNickname();
        this.profileUrl=users.getProfileUrl();

        this.title=webtoon.getTitle();
        this.imageUrl=webtoon.getImageUrl();
        this.pictrWritrNm=webtoon.getPictrWritrNm();
        this.sntncWritrNm=webtoon.getSntncWritrNm();

        this.starVal=starVal;
    }
}
