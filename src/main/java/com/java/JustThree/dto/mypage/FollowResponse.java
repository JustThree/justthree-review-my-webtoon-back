package com.java.JustThree.dto.mypage;
import com.java.JustThree.domain.Follow;
import com.java.JustThree.domain.Users;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponse{
    private Long followId;
    //유저
    private Long usersId;
    private String followingNickname;
    private String followerNickname;
    private String profileUrl;
    private String usersEmail;

    private boolean isFollowing;



    public FollowResponse(Users users,Long followId){
        this.followId=followId;
        this.usersId=users.getUsersId();
        this.usersEmail=users.getUsersEmail();
        this.profileUrl=users.getProfileUrl();
        this.followingNickname=users.getUsersNickname();
    }

//////////////////////////////////////////팔로워와 팔로잉 정보를 받아옴////////////////////
public FollowResponse(Follow follow, int sortNum,boolean isFollowing) {
        this.followId=follow.getFollowId();
        this.isFollowing=isFollowing;
        if(sortNum == 1){
            this.followerNickname=follow.getFollower().getUsersNickname();
            this.usersId=follow.getFollower().getUsersId();
            this.profileUrl=follow.getFollower().getProfileUrl();
            this.usersEmail=follow.getFollower().getUsersEmail();
        }else{
            this.followingNickname = follow.getFollowing().getUsersNickname();
            this.usersId = follow.getFollowing().getUsersId();
            this.profileUrl=follow.getFollowing().getProfileUrl();
            this.usersEmail=follow.getFollowing().getUsersEmail();
        }

    }
    }
//}
