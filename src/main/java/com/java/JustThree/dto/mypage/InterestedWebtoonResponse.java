package com.java.JustThree.dto.mypage;
import com.java.JustThree.domain.Webtoon;
import lombok.*;
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedWebtoonResponse {
    private Long   mastrId;
    private String pictrWritrNm;
    private String sntncWritrNm;
    private String imageUrl;
    private String title;

    public InterestedWebtoonResponse(Webtoon webtoon){
        this.mastrId = webtoon.getMastrId();
        this.pictrWritrNm = webtoon.getPictrWritrNm();
        this.sntncWritrNm = webtoon.getSntncWritrNm();
        this.imageUrl = webtoon.getImageUrl();
        this.title = webtoon.getTitle();
    }
}
