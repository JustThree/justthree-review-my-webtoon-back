package com.java.JustThree.controller;

import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.service.WebtoonService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
public class MainPageController {

    WebtoonService webtoonService;


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
