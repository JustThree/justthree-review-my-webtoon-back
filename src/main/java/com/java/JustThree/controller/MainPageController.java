package com.java.JustThree.controller;

import com.java.JustThree.service.WebtoonService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MainPageController {

    WebtoonService webtoonService;


    @GetMapping("/dbinit")
    public String init(){
        webtoonService.webtoonInit();
        return "db init...";
    }


}
