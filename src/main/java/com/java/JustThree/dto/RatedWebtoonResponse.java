package com.java.JustThree.dto;

import com.java.JustThree.domain.Webtoon;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatedWebtoonResponse {
    private Long   mastrId;
    private String pictrWritrNm;
    private String sntncWritrNm;
    private String imageUrl;
    private int starVal;
    private String title;

    public RatedWebtoonResponse(Webtoon webtoon,int starVal){
        this.mastrId=webtoon.getMastrId();
        this.pictrWritrNm=webtoon.getPictrWritrNm();
        this.sntncWritrNm=webtoon.getSntncWritrNm();
        this.imageUrl=webtoon.getImageUrl();
        this.starVal=starVal;
        this.title=webtoon.getTitle();
    }

}
