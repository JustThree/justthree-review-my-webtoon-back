package com.java.JustThree.service;

import com.java.JustThree.domain.*;
import com.java.JustThree.dto.mypage.*;
import com.java.JustThree.exception.UserNotFoundException;
import com.java.JustThree.repository.*;
import com.java.JustThree.repository.mypage.*;
import com.java.JustThree.repository.StarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            nuser.setUsersNickname(user.getUsersNickname());
            nuser.setUsersPw(user.getUsersPw());
            nuser.setProfileUrl(user.getProfileUrl());
        } catch (Exception e) {
            System.out.println("회원정보 업데이트 실패");
            result = false;
        }
        return result;
    }
    //////////////////////////팔로워 팔로잉 리스트 ////////////////
    public List<FollowResponse> followlist(Long usersId,int sortNum){
        List<Follow> list = switch (sortNum){
            case 2 -> followRepository.findAllByFollower_UsersId(usersId);//팔로잉 목록
            default -> followRepository.findAllByFollowing_UsersId(usersId);//팔로워 목록
        };
        List<FollowResponse> followList = new ArrayList<>();
        for (Follow follow : list){
            FollowResponse dto = new FollowResponse(follow, sortNum);
            followList.add(dto);
        }
        return followList;
    }

    ////////////////////평가 웹툰 리스트////////////////////
    public List<RatedWebtoonResponse> ratedWebtoonlist(Long usersId, int sortNum) {
        List<Star> list = switch (sortNum) {
            case 2 -> starRepository.findByUsers_UsersId_OrderByStarValDesc(usersId);
            case 3 -> starRepository.findByUsers_UsersId_OrderByStarVal(usersId);
            case 4 -> starRepository.findByUsers_UsersIdAndStarVal(usersId, 10);
            default -> starRepository.findByUsers_UsersId(usersId); // 기본순
        };
        List<RatedWebtoonResponse> ratedWebtoonList = new ArrayList<>();
        for (Star star : list) {
            RatedWebtoonResponse dto = new RatedWebtoonResponse(star.getWebtoon(), star.getStarVal());
            ratedWebtoonList.add(dto);
        }
        return ratedWebtoonList;
    }

    //////////////////////팔로우 기능  //////////////////////
    public void toggleFollow(Long followerId, Long followingId) {
        Users follower = usersRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found with id: " + followerId));

        Users following = usersRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following user not found with id: " + followingId));

        // 팔로우 상태 확인 후 팔로우중이면 언팔로우 언팔로우중이면 팔로우
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(follower, following);
        if (isFollowing) {
            unfollowUser(follower, following);
        } else {
            followUser(follower, following);
        }
    }
    private void followUser(Users follower, Users following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }
    private void unfollowUser(Users follower, Users following) {
        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Follow not found"));
        followRepository.delete(follow);
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

    //////////////////////유저 정보 페이지 /////////////////////////
    public UserInfoResponse userinfo(Long usersId) {
        Long followerCount = followRepository.countByFollower_UsersId(usersId);
        Long followingCount = followRepository.countByFollowing_UsersId(usersId);
        Long reviewedCount = reviewRepository.countByUsers_UsersId(usersId);
        Long ratedCount = starRepository.countByUsers_UsersId(usersId);
        Long interestedCount = interestRepository.countByUsers_UsersId(usersId);

        return new UserInfoResponse(usersRepository.findById(usersId).get(),ratedCount, reviewedCount, interestedCount, followerCount, followingCount);
    }





    //////////////////////팔로잉 리스트//////////////////////
    public List<FollowResponse> getFollowingList(Long usersId){
        List<Follow> list = followRepository.findAllByFollower_UsersId(usersId);
        List<FollowResponse>  followingList = new ArrayList<>();
        for(Follow follow : list){
            FollowResponse dto = new FollowResponse(follow.getFollowing(),follow.getFollowId());
            followingList.add(dto);
        }
        return followingList;
    }
    //////////////////////팔로워 리스트//////////////////////
    public List<FollowResponse> getFollowerList(Long usersId){
        List<Follow> list = followRepository.findAllByFollowing_UsersId(usersId);
        List<FollowResponse>  followerList = new ArrayList<>();
        for(Follow follow : list){
            FollowResponse dto = new FollowResponse(follow.getFollower(),follow.getFollowId());
            followerList.add(dto);
        }
        return followerList;
    }
}