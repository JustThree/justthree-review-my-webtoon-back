package com.java.JustThree.service;

import com.java.JustThree.domain.*;
import com.java.JustThree.dto.mypage.*;
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
    public boolean updateUser(Users user, Long usersId) {
        boolean result = true;
        try {
            Users nuser = usersRepository.findById(usersId).get();
            nuser.setUsersEmail(user.getUsersEmail());
            nuser.setUsersPw(user.getUsersPw());
            nuser.setProfileUrl(user.getProfileUrl());
        } catch (Exception e) {
            System.out.println("회원정보 업데이트 실패");
            result = false;
        }
        return result;
    }

    ////////////////////평가 웹툰 리스트////////////////////
    public List<RatedWebtoonResponse> ratedWebtoonlist(Long usersId) {
        List<Star> list = starRepository.findByUsers_UsersId(usersId);
        List<RatedWebtoonResponse> ratedWebtoonList = new ArrayList<>();
        for (Star star : list) {
            RatedWebtoonResponse dto = new RatedWebtoonResponse(star.getWebtoon(), star.getStarVal());
            ratedWebtoonList.add(dto);
        }
        return ratedWebtoonList;
    }

    //////////////////////관심 웹툰 리스트////////////////////

    public List<InterestedWebtoonResponse> interestedWebtoonlist(Long usersId) {
        List<Interest> list = interestRepository.findByUsers_UsersId(usersId);
        List<InterestedWebtoonResponse> interestedWebtoonList = new ArrayList<>();

        for (Interest interest : list) {
            InterestedWebtoonResponse dto = new InterestedWebtoonResponse(interest.getWebtoon());
            interestedWebtoonList.add(dto);
        }
        return interestedWebtoonList;
    }

    //////////////////////웹툰 리뷰 리스트////////////////////
    public List<ReviewedWebtoonResponse> reviewedWebtoonlist(Long usersId) {
        List<Review> list = reviewRepository.findByUsers_UsersId(usersId);
        List<ReviewedWebtoonResponse> reviewedWebtoonList = new ArrayList<>();

        for (Review review : list) {
            Long reviewReplyCount = reviewReplyRepository.countByReview_ReviewId(review.getReviewId());
            Long reviewHeartCount = reviewHeartRepository.countByReview_ReviewId(review.getReviewId());

            ReviewedWebtoonResponse dto = new ReviewedWebtoonResponse(review.getUsers(), review.getWebtoon(), review.getReviewId(), review.getContent(), reviewReplyCount, reviewHeartCount);
            reviewedWebtoonList.add(dto);
        }
        return reviewedWebtoonList;
    }
    //////////////////////팔로잉 리스트//////////////////////
    public List<FollowResponse> getFollowingList(Long usersId){
        List<Follow> list = followRepository.findAllByFollowing_UsersId(usersId);
        List<FollowResponse>  followingList = new ArrayList<>();
        for(Follow follow : list){
            FollowResponse dto = new FollowResponse(follow.getFollowing(),follow.getFollowId());
            followingList.add(dto);
        }
        return followingList;
    }
    //////////////////////팔로워 리스트//////////////////////
    public List<FollowResponse> getFollowerList(Long usersId){
        List<Follow> list = followRepository.findAllByFollower_UsersId(usersId);
        List<FollowResponse>  followerList = new ArrayList<>();
        for(Follow follow : list){
            FollowResponse dto = new FollowResponse(follow.getFollower(),follow.getFollowId());
            followerList.add(dto);
        }
        return followerList;
    }
    //////////////////////유저 정보 페이지 /////////////////////////
    public UserInfoResponse userinfo(Long usersId) {


        Long followerCount = followRepository.countByFollower_UsersId(usersId);
        Long followingCount = followRepository.countByFollowing_UsersId(usersId);
        Long reviewedCount = reviewRepository.countByUsers_UsersId(usersId);
        Long ratedCount = starRepository.countByUsers_UsersId(usersId);
        Long interestedCount = interestRepository.countByUsers_UsersId(usersId);

        UserInfoResponse UserinfoResponse = new UserInfoResponse(usersRepository.findById(usersId).get(), ratedCount, reviewedCount, interestedCount, followerCount, followingCount);
        return UserinfoResponse;
    }
}