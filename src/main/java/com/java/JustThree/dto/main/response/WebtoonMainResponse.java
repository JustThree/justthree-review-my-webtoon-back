package com.java.JustThree.dto.main.response;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WebtoonMainResponse {
    Long masterId;
    String title;
    String writer;
    String imgUrl;
    Float starAvg;
    public static WebtoonMainResponse fromEntity(Webtoon webtoon,Float starAvg){
        return WebtoonMainResponse.builder()
                .masterId(webtoon.getMasterId())
                .title(webtoon.getTitle())
                .writer(webtoon.getPictrWritrNm().equals(webtoon.getSntncWritrNm()) ?
                        webtoon.getPictrWritrNm() :
                        webtoon.getSntncWritrNm() + "/" + webtoon.getPictrWritrNm() )
                .imgUrl(webtoon.getImageUrl())
                .starAvg(starAvg)
                .build();
    }
    public static WebtoonMainResponse fromEntity(Users users){
        return WebtoonMainResponse.builder()
                .masterId(users.getUsersId())
                .title(users.getUsersNickname())
                .writer("")
                .imgUrl(users.getProfileUrl())
                .build();

    }
}
