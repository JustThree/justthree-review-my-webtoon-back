package com.java.JustThree.dto.main.response;

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
    public static WebtoonMainResponse fromEntity(Webtoon webtoon){
        return WebtoonMainResponse.builder()
                .masterId(webtoon.getMastrId())
                .title(webtoon.getTitle())
                .writer(webtoon.getPictrWritrNm().equals(webtoon.getSntncWritrNm()) ?
                        webtoon.getPictrWritrNm() :
                        webtoon.getSntncWritrNm() + "/" + webtoon.getPictrWritrNm() )
                .imgUrl(webtoon.getImageUrl())
                .build();
    }
}
