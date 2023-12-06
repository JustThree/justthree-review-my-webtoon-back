package com.java.JustThree.dto;

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


    private String title; //리뷰 웹툰 제목
    private String imageUrl;//리뷰 웹툰 사진

    private int starVal;//별점

}
