package com.java.JustThree.service;

import com.java.JustThree.domain.Interest;
import com.java.JustThree.domain.Review;
import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.mypage.InterestedWebtoonResponse;
import com.java.JustThree.dto.mypage.RatedWebtoonResponse;
import com.java.JustThree.dto.mypage.ReviewedWebtoonResponse;
import com.java.JustThree.dto.mypage.UserInfoResponse;
import com.java.JustThree.repository.*;
import com.java.JustThree.repository.mypage.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UsersRepository usersRepository;
    private final StarRepository starRepository;
    private final InterestRepository interestRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewHeartRepository reviewHeartRepository;
    private final FollowRepository followRepository;


    //////////////////////회원 정보 수정 Update////////////////////
    @Transactional
    public boolean updateUser(Users user,Long usersId){
        boolean result = true;
        try{
            Users nuser = usersRepository.findById(usersId).get();
            nuser.setUsersNickname(user.getUsersNickname());
            nuser.setUsersPw(user.getUsersPw());
            nuser.setProfileUrl(user.getProfileUrl());
        } catch (Exception e){
            System.out.println("회원정보 업데이트 실패");
            result=false;
        }
        return result;
    }

    ////////////////////평가 웹툰 리스트////////////////////
    public List<RatedWebtoonResponse>ratedWebtoonlist(Long usersId){
        List<Star> list = starRepository.findByUsers_UsersId(usersId);
        List<RatedWebtoonResponse> ratedWebtoonList= new ArrayList<>();
        for (Star star : list) {
            RatedWebtoonResponse dto = new RatedWebtoonResponse(star.getWebtoon(), star.getStarVal());
            ratedWebtoonList.add(dto);
        }
        return ratedWebtoonList;
    }



    //////////////////////관심 웹툰 리스트////////////////////

    public List<InterestedWebtoonResponse>interestedWebtoonlist(Long usersId){
        List<Interest> list = interestRepository.findByUsers_UsersId(usersId);
        List<InterestedWebtoonResponse> interestedWebtoonList = new ArrayList<>();

        for(Interest interest : list){
            InterestedWebtoonResponse dto = new InterestedWebtoonResponse(interest.getWebtoon());
            interestedWebtoonList.add(dto);
        }
        return interestedWebtoonList;
    }
    //////////////////////웹툰 리뷰 리스트////////////////////
    public List<ReviewedWebtoonResponse>reviewedWebtoonlist(Long usersId){
        List<Review> list = reviewRepository.findByUsers_UsersId(usersId);
        List<ReviewedWebtoonResponse> reviewedWebtoonList = new ArrayList<>();

        for (Review review :list){
            Long reviewReplyCount = reviewReplyRepository.countByReview_ReviewId(review.getReviewId());
            Long reviewHeartCount = reviewHeartRepository.countByReview_ReviewId(review.getReviewId());

            ReviewedWebtoonResponse dto = new ReviewedWebtoonResponse(review.getUsers(),review.getWebtoon(), review.getReviewId(), review.getContent(),reviewReplyCount,reviewHeartCount);
            reviewedWebtoonList.add(dto);
        }
        return reviewedWebtoonList;
    }
    //////////////////////유저 정보 페이지 /////////////////////////
    public UserInfoResponse userinfo(Long usersId){


        Long followerCount = followRepository.countByFollower_UsersId(usersId);
        Long followingCount = followRepository.countByFollowing_UsersId(usersId);
        Long reviewedCount = reviewRepository.countByUsers_UsersId(usersId);
        Long ratedCount = starRepository.countByUsers_UsersId(usersId);
        Long interestedCount = interestRepository.countByUsers_UsersId(usersId);

        UserInfoResponse UserinfoResponse = new UserInfoResponse(usersRepository.findById(usersId).get(),ratedCount,reviewedCount,interestedCount,followerCount,followingCount);
        return UserinfoResponse;
    }
    //////////////////////팔로워 리스트//////////////////////

    //////////////////////팔로잉 리스트//////////////////////




}
