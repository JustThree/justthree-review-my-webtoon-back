package com.java.JustThree.dto;

import com.java.JustThree.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
/**
 * 이걸 어디에 쓸까 고민중
 */
public class UsersResponse {
    private Long usersId;
    private String usersEmail;
    private String usersNickname;
    private String profileUrl;
    private String created;
    private int statusCode;

    public Users toEntity(UsersResponse usersResponse){
        return Users.builder()
                .usersEmail(this.getUsersEmail())
                .usersId(this.getUsersId())
                .build();
    };
}
