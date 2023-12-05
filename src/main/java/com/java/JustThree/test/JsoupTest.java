package com.java.JustThree.test;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JsoupTest {

    public static void main(String[] args) {

        Map<String, Object> hashMap = new HashMap<>();
        Set<String> set19 = new HashSet<>();
        for (int idx=0; idx<=55000 ; idx+= 100) {
            HttpURLConnection conn = null;
            String viewItemCntVal = "100";
            String listSeCdVal = "1"; // 1  :  웹툰 2  :  도서(만화책) 3  :  잡지 4 :  영화 5  :  드라마 6  :  게임 7 :  공연,전시 8  :  행사(전시,행사,축제,컨퍼런스,공모전) 9  :  상품
            String prvKeyVal =  ""; // properties 적기
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
                for (Object ele :
                        jsonArray) {
                    JSONObject js = new JSONObject(ele.toString());
                    // 만약 해당 성인이 거나 해당 json의 키가 포홤되지 않으면
                    String tittlePicWriSnWri = js.get("title").toString() + "_" + js.get("pictrWritrNm").toString() + "_" + js.get("sntncWritrNm").toString();
                    // 함수화해서 리팩토링///
                    if (!js.get("ageGradCdNm").equals("19세 이상")&& !js.get("ageGradCdNm").equals("확인필요")&&
                            !js.get("pltfomCdNm").equals("레알코믹스")&&!js.get("pltfomCdNm").equals("셀툰")&&!js.get("pltfomCdNm").equals("야툰")
                            &&!js.get("title").toString().contains("개정판")&&!js.get("title").toString().contains("섹스")&&!js.get("title").toString().contains("섹기")&&!js.get("title").toString().contains("섹파")&&!js.get("title").toString().contains("야썰")
                            &&!js.get("mainGenreCdNm").equals("동성애") &&!js.get("mainGenreCdNm").equals("BL") &&!js.get("mainGenreCdNm").equals("GL")
                            &&!js.get("outline").toString().contains("개정판")&&!js.get("outline").toString().contains("섹스")&&!js.get("outline").toString().contains("섹기")&&!js.get("outline").toString().contains("섹파")&&!js.get("outline").toString().contains("야썰")
                    ) {
                        String 규장각Url = "https://www.kmas.or.kr/archive/book/" + js.get("mastrId");
                        Connection jsoupConn = Jsoup.connect(규장각Url);
                        Document document = jsoupConn.get();
                        Elements elements = document.getElementsByClass("dv-table w100p vd-table");
                        Elements aTags = elements.select("a");
                        String[] split = aTags.toString().split(" ");
                        if (!hashMap.containsKey(tittlePicWriSnWri)) {
                            hashMap.put(tittlePicWriSnWri, js.toString());
                            for (String s :
                                    split) {
                                if (s.contains("https")) {
                                    int front = s.indexOf("'");
                                    int back = s.lastIndexOf("'");
                                }
                            }
                        }
                    } else {
                        // 밴 리스트 추가
                        set19.add(tittlePicWriSnWri);
                        // 만약에 해시셋에 이미 데이터가 들어갓다면 삭제
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String ele:
                set19) {
            hashMap.remove(ele);
        }

        System.out.println(1);
    }
}
