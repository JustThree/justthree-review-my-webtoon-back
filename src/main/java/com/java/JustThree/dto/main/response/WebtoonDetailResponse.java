package com.java.JustThree.dto.main.response;


import com.java.JustThree.domain.Webtoon;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


/*
필요 한것
제목,
작가
장르
평점
내용
사진주소
링크url
리뷰 내역
 */
@Builder
@Getter
public class WebtoonDetailResponse {
    public String title;
    public String writer;
    public String genre;
    public float avgRating;
    public String outline;
    public String imgUrl;
    public String links;
//    public List<Review> Reviews;
    public static WebtoonDetailResponse fromEntity(Webtoon webtoon){
        return WebtoonDetailResponse.builder()
                .title(webtoon.getTitle())
                .writer("글:" + webtoon.getSntncWritrNm() + "그림:" + webtoon.getPictrWritrNm())
                .genre(webtoon.getMainGenreCdNm())
                .avgRating(3.4f)
                .outline(webtoon.getOutline())
                .imgUrl(webtoon.getImageUrl())
                .links(webtoon.getImageUrl())
                .build();
    }
}
