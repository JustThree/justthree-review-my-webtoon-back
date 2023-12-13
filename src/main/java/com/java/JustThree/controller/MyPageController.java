package com.java.JustThree.controller;

import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.mypage.*;
import com.java.JustThree.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@CrossOrigin
public class MyPageController {
    private final MyPageService service;
    private final PasswordEncoder passwordEncoder;
    //////////////////////////////////////////////////유저 정보 페이지/////////////////////////////////////////////////////////////
    @GetMapping("/userinfo/{usersId}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Long usersId){
        UserInfoResponse userInfoResponse = service.userinfo(usersId);
        return ResponseEntity.ok(userInfoResponse);
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
        Long user_Id = user.getUsersId();//토큰으로 아이디 받아오는걸로 바꿔야하ㅁ
//        int user_Id = jwtService.getId(token);
        user.setUsersPw(passwordEncoder.encode(user.getUsersPw()));//시큐리띠
        boolean result = service.updateUser(user,user_Id);

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
    //////////////////////////////////////////////////평가 웹툰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/rated/{usersId}")
    public ResponseEntity<List<RatedWebtoonResponse>> ratedWebtoon(@PathVariable Long usersId , @RequestParam (defaultValue = "1")int sortNum) {
        List<RatedWebtoonResponse> ratedWebtoonList = service.ratedWebtoonlist(usersId,sortNum);
        return ResponseEntity.ok(ratedWebtoonList);
    }
    /////////////////////////////////////////////////팔로우 목록 //////////////////////////////////
//    @GetMapping("/follow/{usersId}")
//    public ResponseEntity<List<FollowResponse>> followlist(@PathVariable Long usersId,@RequestParam(defaultValue = "1")int sortNum){
//        List<FollowResponse> follow=service.followList(usersId,sortNum);
//        return ResponseEntity.ok(follow);
//    }
    ////////////////////////////////////////////////팔로잉 하는 목록/////////////////////////////////////////////////////////////
    @GetMapping("/following/{usersId}")
    public List<FollowResponse> getFollowingList(@PathVariable Long usersId) {
        return service.getFollowingList(usersId);
    }
    ////////////////////////////////////////////////팔로워 목록 ////////////////////////////////////////////////////////////////
    @GetMapping("/follower/{usersId}")
    public List<FollowResponse> getFollowList(@PathVariable Long usersId){
        return service.getFollowerList(usersId);
    }



}
