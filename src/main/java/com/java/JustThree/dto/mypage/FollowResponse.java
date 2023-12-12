package com.java.justthree.dto.mypage;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponse{
    private Long FollowId;

    //유저
    private String usersNickname;
    private String profileUrl;
    private String userEmail;




//    public FollowResponse(Users users){
//    }
}
