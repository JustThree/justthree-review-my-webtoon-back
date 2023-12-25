package com.java.JustThree.dto.admin;

import com.java.JustThree.domain.Webtoon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonRateResponse {
    private String title;
    private Float rate;

    public static WebtoonRateResponse fromEntity(Webtoon webtoon,Float starAvg){
        return WebtoonRateResponse.builder()
                .title(webtoon.getTitle())
                .rate(starAvg)
                .build();
    }
}