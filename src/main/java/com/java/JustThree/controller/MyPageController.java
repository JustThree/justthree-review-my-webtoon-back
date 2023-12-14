package com.java.JustThree.controller;

import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.mypage.*;
import com.java.JustThree.service.MyPageService;
import com.java.JustThree.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@CrossOrigin
public class MyPageController {
    private final MyPageService service;
    private final BCryptPasswordEncoder passwordEncoder;
  private final UsersService usersService;
    //////////////////////////////////////////////////유저 정보 페이지/////////////////////////////////////////////////////////////
    @GetMapping("/userinfo/{usersId}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Long usersId,@RequestHeader("Authorization") String token){
        UserInfoResponse userInfoResponse = myPageService.userinfo(usersId);

        return ResponseEntity.ok(userInfoResponse);
    }
    //////////////////////////////////////////////////작성 리뷰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/reviewed/{usersId}")
    public ResponseEntity<List<ReviewedWebtoonResponse>> reviewedWebtoon(@PathVariable Long usersId){
        List<ReviewedWebtoonResponse> reviewedWebtoonList=myPageService.reviewedWebtoonlist(usersId);
        return ResponseEntity.ok(reviewedWebtoonList);
    }
    //////////////////////////////////////////////////관심 웹툰 목록/////////////////////////////////////////////////////////////
    @GetMapping("/interested/{usersId}")
    public ResponseEntity<List<InterestedWebtoonResponse>> interestedWebtoon(@PathVariable Long usersId){
        List<InterestedWebtoonResponse> interestedWebtoonList=myPageService.interestedWebtoonlist(usersId);
        return ResponseEntity.ok(interestedWebtoonList);
    }
    //////////////////////////////////////////////////유저 정보 업뎃/////////////////////////////////////////////////////////////
    @PutMapping("/update")
    public ResponseEntity<CudResponse> updateUser(@RequestHeader(value="Authorization",required = false)String token, @RequestBody Users user){
        CudResponse response = new CudResponse();
        Long usersId = usersService.getUserInfo(token).getUsersId();
        user.setUsersPw(passwordEncoder.encode(user.getUsersPw()));//시큐리띠
        boolean result = myPageService.updateUser(user,usersId);
        if (result){
            response.setSuccess(true);
            response.setMessage("수정완료");
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
        List<RatedWebtoonResponse> ratedWebtoonList = myPageService.ratedWebtoonlist(usersId,sortNum);
        return ResponseEntity.ok(ratedWebtoonList);
    }
    //////////////////////////////////////////////// 팔로우 하기 ////////////////////////////////////
    @PostMapping("/follow")
    public void toggleFollow(@RequestParam Long followerId,@RequestParam Long followingId) {
        myPageService.toggleFollow(followerId,followingId);
    }
    ////////////////////////////////////////////////팔로잉 하는 목록/////////////////////////////////////////////////////////////
    @GetMapping("/following/{usersId}")
    public List<FollowResponse> getFollowingList(@PathVariable Long usersId) {
        return myPageService.getFollowingList(usersId);
    }
    ////////////////////////////////////////////////팔로워 목록 ////////////////////////////////////////////////////////////////
    @GetMapping("/follower/{usersId}")
    public List<FollowResponse> getFollowerList(@PathVariable Long usersId){
        return myPageService.getFollowerList(usersId);
    }
    //////////////////////////////////////////////팔로잉 팔로워 목록 ///////////////////////////////
    @GetMapping("/follow/{usersId}")
    public ResponseEntity<List<FollowResponse>> getFollowList(@PathVariable Long usersId, @RequestParam(defaultValue = "1") int sortNum) {
        try {
            List<FollowResponse> followList = myPageService.followlist(usersId,sortNum);
            return ResponseEntity.ok(followList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
