package com.java.JustThree.service;

import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.dto.main.response.WebtoonDetailResponse;
import com.java.JustThree.dto.main.response.WebtoonMainResponse;
import com.java.JustThree.repository.WebtoonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WebtoonService {
    @Value("${gujang}")
    private String gujang;
    final
    WebtoonRepository webtoonRepository;

    public WebtoonService(WebtoonRepository webtoonRepository) {
        this.webtoonRepository = webtoonRepository;
    }
    // 페이지 단건 조회
    @Transactional
    public WebtoonDetailResponse getWebtoonDetail(long id){
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(IllegalAccessError::new);
        if (webtoon.getView()==null){
            webtoon.setView(1L);
        }else {
            webtoon.setView(webtoon.getView() + 1);
        }
        return WebtoonDetailResponse.fromEntity(webtoon);
    }
    // webtoon 전체 조회
    public Page<WebtoonMainResponse> getWebtoonPage(Pageable pageable, String genre, String order){
        Page<WebtoonMainResponse> webtoonMainResponsePage;
        String orderVal;
        switch (order){
            case "latest" :  orderVal = "masterId";
            break;
            case "like": orderVal = "view";
                break;
            case "rate": orderVal = "masterId";
                break;
            default : orderVal = "masterId";
                break;
        }

        System.out.println(1);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,
                orderVal));
        webtoonMainResponsePage = switch (genre) {
            case "fantasy" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "판타지", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            case "romance" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "이성애", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            case "school" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "학원", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            case "daily" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "일상", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            case "comic" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "코믹", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            case "martialarts" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                            ("19세 이상", "무협", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
            default -> webtoonRepository.findByAgeGradCdNmIsNot
                            ("19세 이상", pageable)
                    .map(WebtoonMainResponse::fromEntity); // 19세 이상 인웹툰 제외한 글 다 꺼내서 WebtoonMainResponse 으로 변환
        };
        return  webtoonMainResponsePage;
    }
    // 웹툰 키워드로 리스트 조회
    public List<WebtoonMainResponse> getWebtoonKeyword(Pageable pageable,String keyword){
        List<WebtoonMainResponse> webtoonMainResponseList = null;
        webtoonMainResponseList = switch (keyword) {
            case "recent" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByPusryBeginDeDesc
                            ("19세 이상", pageable)
                    .stream().map((WebtoonMainResponse::fromEntity)).toList();
            case "recentend" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByPusryEndDeDesc
                            ("19세 이상", pageable)
                    .stream().map((WebtoonMainResponse::fromEntity)).toList();
            case "fantasy" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMasterIdDesc
                            ("19세 이상", "판타지", pageable)
                    .stream().map((WebtoonMainResponse::fromEntity)).toList();
            case "love" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMasterIdDesc
                            ("19세 이상", "이성애", pageable)
                    .stream().map((WebtoonMainResponse::fromEntity)).toList();
            case "famous" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByViewDesc
                            ("19세 이상", pageable)
                    .stream().map((WebtoonMainResponse::fromEntity)).toList();
            default -> webtoonMainResponseList;
        };
        return webtoonMainResponseList;
    }

    // 웹툰 초기화
    public void webtoonInit(Map<String, Webtoon> mapJson, Set<String> setNotNormal, int idx) {
        List<Webtoon> webtoons = new ArrayList<>();
        HttpURLConnection conn = null;
        String viewItemCntVal = "100";
        String listSeCdVal = "1"; // 1  :  웹툰 2  :  도서(만화책) 3  :  잡지 4 :  영화 5  :  드라마 6  :  게임 7 :  공연,전시 8  :  행사(전시,행사,축제,컨퍼런스,공모전) 9  :  상품
        String prvKeyVal = gujang; // properties 적기
        String page = String.valueOf(idx);// 페이지를 viewItemCntVal 씩 늘려야댐..
        try {
            String openApiUrl = "https://kmas.or.kr/openapi/search/rgDtaMasterList";
            openApiUrl += "?";
            openApiUrl += "prvKey" + "=" + prvKeyVal + "&";
            openApiUrl += "listSeCd" + "=" + listSeCdVal + "&";
            openApiUrl += "viewItemCnt" + "=" + viewItemCntVal + "&";
            openApiUrl += "pageNo" + "=" + page;
            URL url = new URL(openApiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            // Read the response into a StringBuilder
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            try {
                jsonObject = new JSONObject(response.toString());
                jsonArray = jsonObject.getJSONArray("itemList");
            } catch (Exception e) {
                System.out.println(jsonObject);
                throw new RuntimeException(e);
            }
            conn.disconnect();
            for (Object ele :
                    jsonArray) {
                JSONObject js = new JSONObject(ele.toString());
                // 중복 만화 체크 키
                String tittlePicWriSnWri = js.get("title").toString() + "_" + js.get("pictrWritrNm").toString() + "_" + js.get("sntncWritrNm").toString();
                String 규장각Url = "https://www.kmas.or.kr/archive/book/" + js.get("mastrId");
                String link = "";
                String newLink = "";
                // 필터
                if (js.get("ageGradCdNm").equals("19세 이상") || js.get("ageGradCdNm").equals("확인필요")// 함수화해서 리팩토링 하기
                        || js.get("pltfomCdNm").equals("레알코믹스") || js.get("pltfomCdNm").equals("셀툰") || js.get("pltfomCdNm").equals("야툰")
                        || js.get("title").toString().contains("에로")||js.get("title").toString().contains("개정판") || js.get("title").toString().contains("섹스") || js.get("title").toString().contains("섹기") || js.get("title").toString().contains("섹파") ||js.get("title").toString().contains("색툰")|| js.get("title").toString().contains("야썰")
                        || js.get("mainGenreCdNm").equals("동성애") || js.get("mainGenreCdNm").equals("BL") || js.get("mainGenreCdNm").equals("GL")
                        || js.get("outline").toString().contains("에로")||js.get("outline").toString().contains("개정판") || js.get("outline").toString().contains("섹스") || js.get("outline").toString().contains("섹기") || js.get("outline").toString().contains("섹파") ||js.get("outline").toString().contains("색툰")|| js.get("outline").toString().contains("야썰")) {
                    // 19세인경우 set에 추가
                    setNotNormal.add(tittlePicWriSnWri);
                } else {
                    // 아니면 jsoup연결후 주소 가져오기
                    Connection jsoupConn = Jsoup.connect(규장각Url);
                    Document document = jsoupConn.get();
                    Elements elements = document.getElementsByClass("dv-table w100p vd-table");
                    Elements aTags = elements.select("a");
                    String[] split = aTags.toString().split(" ");
                    for (String s :
                            split) {
                        if (s.contains("http")) {
                            int front = s.indexOf("'");
                            int back = s.lastIndexOf("'");
                            link = s.substring(front + 1, back);
                            break;
                        }
                    }
                }

                if (mapJson.containsKey(tittlePicWriSnWri)) {
                    String oldLink = mapJson.get(tittlePicWriSnWri).getUrls();
                    newLink = oldLink + "+" + js.get("pltfomCdNm") + "$" + link;
                    Optional<Webtoon> byId = webtoonRepository.findById(mapJson.get(tittlePicWriSnWri).getMasterId());
                    if (byId.isPresent()) {
                        Webtoon webtoon = byId.get();
                        webtoon.setUrls(newLink);
                        webtoons.add(webtoon);
                    } else {
                    }
                } else {
                    newLink = js.get("pltfomCdNm") + "$" + link;
                    Webtoon webtoon = Webtoon.builder()
                            .masterId(Long.parseLong(js.get("mastrId").toString()))
                            .title(js.get("title").toString())
                            .pictrWritrNm(js.get("pictrWritrNm").toString())
                            .sntncWritrNm(js.get("sntncWritrNm").toString())
                            .mainGenreCdNm(js.get("mainGenreCdNm").toString())
                            .outline(js.get("outline").toString())
                            .pltfomCdNm(js.get("pltfomCdNm").toString())
                            .ageGradCd(js.get("ageGradCd").toString())
                            .ageGradCdNm(js.get("ageGradCdNm").toString())
                            .pusryBeginDe(js.get("pusryBeginDe").toString())
                            .pusryEndDe(js.get("pusryEndDe").toString())
                            .fnshYn(js.get("fnshYn").toString())
                            .webtoonPusryYn(js.get("webtoonPusryYn").toString())
                            .orginlNationCdNm(js.get("orginlNationCdNm").toString())
                            .urls(newLink) // 성인일 경우 url ... 로그인 해야댐.. 나중에 url 넣기
                            .imageUrl(js.get("imageDownloadUrl").toString())
                            .build();
                    webtoons.add(webtoon);
                    mapJson.put(tittlePicWriSnWri, webtoon);
                }
                // 엔티티 생성

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        for (Webtoon webtoon:
             webtoons) {
            //js.get("title").toString() + "_" + js.get("pictrWritrNm").toString() + "_" + js.get("sntncWritrNm").toString()
            if (setNotNormal.contains(webtoon.getTitle() + "_" + webtoon.getPictrWritrNm() + "_" + webtoon.getSntncWritrNm())){
                Optional<Webtoon> byId = webtoonRepository.findById(webtoon.getMasterId());
            if (byId.isPresent()) {
                byId.get().setAgeGradCd("4");
                byId.get().setAgeGradCdNm("19세 이상");
            }
        }

        webtoonRepository.saveAll(webtoons);
        }
    }
}