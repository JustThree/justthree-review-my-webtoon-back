package com.java.JustThree.service;

import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.repository.WebtoonRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Service
public class WebtoonService {
    WebtoonRepository webtoonRepository;
    @Transactional
    public void webtoonInit(){
            Map<String, Webtoon> mapJson = new HashMap<>();
            Set<String> setNotNormal = new HashSet<>();
            for (int idx=0; idx<=55000 ; idx+= 100) { // idx 상한선 나중에 바꾸기
                HttpURLConnection conn = null;
                String viewItemCntVal = "100";
                String listSeCdVal = "1"; // 1  :  웹툰 2  :  도서(만화책) 3  :  잡지 4 :  영화 5  :  드라마 6  :  게임 7 :  공연,전시 8  :  행사(전시,행사,축제,컨퍼런스,공모전) 9  :  상품
                String prvKeyVal =  "6477523599ca34f2624e4da94674db35"; // properties 적기
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
                    try {
                        jsonObject = new JSONObject(response.toString());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("itemList");
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
                        if (js.get("ageGradCdNm").equals("19세 이상")||js.get("ageGradCdNm").equals("확인필요")// 함수화해서 리팩토링 하기
                                ||js.get("pltfomCdNm").equals("레알코믹스")||js.get("pltfomCdNm").equals("셀툰")||js.get("pltfomCdNm").equals("야툰")
                                ||js.get("title").toString().contains("개정판")||js.get("title").toString().contains("섹스")||js.get("title").toString().contains("섹기")||js.get("title").toString().contains("섹파")||js.get("title").toString().contains("야썰")
                                ||js.get("mainGenreCdNm").equals("동성애") ||js.get("mainGenreCdNm").equals("BL")||js.get("mainGenreCdNm").equals("GL")
                                ||js.get("outline").toString().contains("개정판")||js.get("outline").toString().contains("섹스")||js.get("outline").toString().contains("섹기")||js.get("outline").toString().contains("섹파")||js.get("outline").toString().contains("야썰")) {
                            // 19세인경우 set에 추가
                            setNotNormal.add(tittlePicWriSnWri);
                        } else {
                            // 아니면 jsoup연결후 주소 가져오기
                            Connection jsoupConn = Jsoup.connect(규장각Url);
                            Document document = jsoupConn.get();
                            Elements elements = document.getElementsByClass("dv-table w100p vd-table");
                            Elements aTags = elements.select("a");
                            String[] split = aTags.toString().split(" ");
                            for (String s:
                                 split) {
                                if (s.contains("https")) {
                                    int front = s.indexOf("'");
                                    int back = s.lastIndexOf("'");
                                    link = s.substring(front+1,back);
                                    break;
                                }
                            }
                        }

                        if (mapJson.containsKey(tittlePicWriSnWri)) {
                            String oldLink = mapJson.get(tittlePicWriSnWri).getUrls();
                            newLink = oldLink + "_" + js.get("pltfomCdNm") + "$" + link;
                        }
                        else {
                            newLink = js.get("pltfomCdNm") + "$" + link;
                        }
                        // 엔티티 생성
                        Webtoon webtoon = Webtoon.builder()
                                .mastrId(Long.parseLong(js.get("mastrId").toString()))
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
                        mapJson.put(tittlePicWriSnWri, webtoon);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            // 마지막으로 성인분류 됬으면 => 성인 코드 변경
        for (String key:
             setNotNormal) {
            mapJson.get(key).setAgeGradCd("4");
            mapJson.get(key).setAgeGradCdNm("19세 이상");
        }
        // 리포지토리에 저장
        for (String key:
             mapJson.keySet()) {
            webtoonRepository.save(mapJson.get(key));
        }
    }
}