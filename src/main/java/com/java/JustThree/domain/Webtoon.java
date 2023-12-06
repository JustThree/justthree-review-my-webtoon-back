package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.IdGeneratorType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Webtoon {
    @Id
    @Column(name = "master_id")
    private Long mastrId;           // 웹툰아이디
    private String title;           // 서명
    @Column(name = "pictr_writr_nm")
    private String pictrWritrNm;    //그림작가
    @Column(name = "sntnc_writr_nm")
    private String sntncWritrNm;    // 글작가
    @Column(name = "main_genre_cd_nm")
    private String mainGenreCdNm;   // 대표장르코드명
    @Column(columnDefinition = "TEXT")
    private String outline;         // 줄거리
    @Column(name = "pltfom_cd_nm")
    private String pltfomCdNm;      // 플랫폼코드명
    @Column(name = "age_grad_cd")
    @Setter
    private String ageGradCd;       // 연령등급코드
    @Column(name = "age_grad_cd_nm")
    @Setter
    private String ageGradCdNm;     // 연령등급코드명
    @Column(name = "pusry_begin_de")
    private String pusryBeginDe;    // 연재개시일자
    @Column(name = "pusry_end_de")
    private String pusryEndDe;      // 연재종료일자
    @Column(name = "fnsh_yn")
    private String fnshYn;          // 완결여부
    @Column(name = "webtoon_pusry_yn")
    private String webtoonPusryYn;  // 웹툰연재여부
    @Column(name = "orginl_nation_cd_nm")
    private String orginlNationCdNm;// 원작국가코드명
    @Column(name = "imageUrl")
    private String imageUrl;
    @Column(name = "urls")
    @Setter
    private String urls;

}