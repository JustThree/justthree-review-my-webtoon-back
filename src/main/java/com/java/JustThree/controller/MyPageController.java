package com.java.JustThree.controller;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import com.java.JustThree.dto.CudResponse;
import com.java.JustThree.dto.InterestedWebtoonResponse;
import com.java.JustThree.dto.RatedWebtoonResponse;
import com.java.JustThree.dto.ReviewedWebtoonResponse;
import com.java.JustThree.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@CrossOrigin
public class MyPageController {
    private final MyPageService service;
    private final PasswordEncoder passwordEncoder;

    //////////////////////////////////////////////////평가 웹툰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/rated/{usersId}")
    public ResponseEntity<List<RatedWebtoonResponse>> ratedWebtoon(@PathVariable Long usersId) {
        List<RatedWebtoonResponse> ratedWebtoonList = service.ratedWebtoonlist(usersId);
        return ResponseEntity.ok(ratedWebtoonList);
    }
    //////////////////////////////////////////////////작성 리뷰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/reviewed/{usersId}")
    public ResponseEntity<List<ReviewedWebtoonResponse>> reviewedWebtoon(@PathVariable Long usersId){
        List<ReviewedWebtoonResponse> reviewedWebtoonList=service.reviewedWebtoonlist(usersId);
        return ResponseEntity.ok(reviewedWebtoonList);
    }
    //////////////////////////////////////////////////관심 웹툰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/interested/{usersId}")
    public ResponseEntity<List<InterestedWebtoonResponse>> interestedWebtoon(@PathVariable Long usersId){
        List<InterestedWebtoonResponse> interestedWebtoonList=service.interestedWebtoonlist(usersId);
        return ResponseEntity.ok(interestedWebtoonList);
    }
    //////////////////////////////////////////////////유저 정보 업뎃/////////////////////////////////////////////////////////////
    @PutMapping("/update")
    public ResponseEntity<CudResponse> updateUser(@RequestHeader(value="Authorization",required = false)String token, @RequestBody Users user){
        CudResponse response = new CudResponse();
        int user_Id = Math.toIntExact(user.getUsersId());//토큰으로 아이디 받아오는걸로 바꿔야하ㅁ
        //int user_Id = jwtService.getId(token);
        user.setUsersPw(passwordEncoder.encode(user.getUsersPw()));//시큐리띠
        boolean result = service.updateUser(user, user_Id);

        if (result){
            response.setSuccess(true);
            response.setMessage("개인정보 변경 성공 \n 다시 로그인 해주세요");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.setSuccess(false);
            response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






}
