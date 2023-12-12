package com.java.JustThree.dto.mypage;
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
    private String usersNickname;
    private String profileUrl;
    private String usersEmail;



    public FollowResponse(Users users,Long followId){
        this.usersEmail=users.getUsersEmail();
        this.profileUrl=users.getProfileUrl();
        this.usersNickname=users.getUsersNickname();
        this.followId=followId;
    }
    }
//}
