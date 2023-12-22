package com.java.JustThree.dto.mypage;

import com.java.JustThree.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdateUserRequest {
    private Users users;
    private String usersId;
    private String usersNickname;
    private String profilesUrl;
    private MultipartFile[] imageFiles;
}
