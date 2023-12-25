package com.java.JustThree.dto.mypage;

import com.java.JustThree.domain.Users;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String profileUrl;//해당 유저 프사
    private String usersNickname;//해당 유저 닉네임
    private boolean isFollowing;
    private String usersEmail;
    private Long ratedCount;//평가한 웹툰 수
    private Long reviewedCount;//남긴 리뷰 수
    private Long interestedCount;//관심 웹툰 수
    private Long followerCount;//팔로워 수
    private Long followingCount;//팔로잉 수


    public UserInfoResponse(Users users,boolean isFollowing, Long ratedCount, Long reviewedCount, Long interestedCount, Long followerCount, Long followingCount){
        this.profileUrl=users.getProfileUrl();
        this.usersNickname=users.getUsersNickname();
        this.usersEmail=users.getUsersEmail();
        this.ratedCount=ratedCount;
        this.reviewedCount=reviewedCount;
        this.interestedCount=interestedCount;

        this.followerCount=followerCount;
        this.followingCount=followingCount;
        this.isFollowing=isFollowing;

    }
}
