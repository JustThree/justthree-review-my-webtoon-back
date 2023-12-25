package com.java.JustThree.dto.main.response;


import com.java.JustThree.domain.Webtoon;
import lombok.Builder;
import lombok.Getter;


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
    public Float avgRating;
    public String outline;
    public String imgUrl;
    public String links;
    public Integer countStar;
    public Integer userStar;
    public boolean isAddInterest;
    public boolean ageCheck;
    public static WebtoonDetailResponse fromEntity(Webtoon webtoon,
                                                   Float starAvg,
                                                   Integer countStar,
                                                   Integer userStar,
                                                   boolean isAddInterest,
                                                   boolean ageCheck){
        return WebtoonDetailResponse.builder()
                .title(webtoon.getTitle())
                .writer("글:" + webtoon.getSntncWritrNm() + " /그림:" + webtoon.getPictrWritrNm())
                .genre(webtoon.getMainGenreCdNm())
                .avgRating(starAvg) // 수정필요
                .outline(webtoon.getOutline())
                .imgUrl(webtoon.getImageUrl())
                .links(webtoon.getUrls())
                .countStar(countStar)
                .userStar(userStar)
                .isAddInterest(isAddInterest)
                .ageCheck(ageCheck)
                .build();

    }
}
