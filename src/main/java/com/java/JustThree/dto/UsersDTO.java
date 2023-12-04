package com.java.JustThree.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private Integer users_id;
    private String users_nickname;
    private String users_email;
    private String profile_url;
    private LocalDateTime created;
    private int status_code;
}
