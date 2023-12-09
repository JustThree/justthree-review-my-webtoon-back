package com.java.JustThree.controller;

import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.dto.main.response.WebtoonDetailResponse;
import com.java.JustThree.service.WebtoonService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api/webtoon")
@RestController
@AllArgsConstructor
public class MainPageController {
    WebtoonService webtoonService;


    // 단건 조회
    // 성인웹툰 거르기...
    @GetMapping("/{id}")
    public ResponseEntity<?> webtoonDetail(@PathVariable(name = "id") Long id){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonDetail(id));
        } catch (IllegalArgumentException e){
            return ResponseEntity
                    .notFound()
                    .header("error",e.getMessage())
                    .build();
        } catch (NullPointerException e){
            System.out.println(1);
            return ResponseEntity
                    .status(404)
                    .header("error",e.getMessage())
                    .build();
        }
    }
    @GetMapping("/webtoonlist")
    public ResponseEntity<?> webtoonList(@RequestParam(name = "keyword") String keyword){
        System.out.println(keyword);
        try {
            return ResponseEntity.ok()
                            .body(webtoonService.getWebtoonMainPage(keyword));
        } catch (Exception e){
            return  null;
        }
    }
//    @GetMapping("/dbinit")
//    public String init(){
//        Map<String, Webtoon> mapJson = new HashMap<>();
//        Set<String> setNotNormal = new HashSet<>();
//        for (int idx=0; idx<=55000 ; idx+= 100) { // idx 상한선 나중에 바꾸기
//            if (idx % 1000 == 0) {
//                System.out.println("" + idx + "번 진행중");
//            }
//            webtoonService.webtoonInit(mapJson, setNotNormal, idx);
//        }
//        return "db init...";
//    }


}
