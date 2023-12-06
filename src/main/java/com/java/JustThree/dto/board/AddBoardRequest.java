package com.java.JustThree.dto.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.domain.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardRequest {
    //private Long boardId;
    private String title;
    private String content;
    //private long viewCount;
    //private LocalDateTime created;
    //private LocalDateTime updated;
    private int noticeYn;
    private Users users;
    //save image request dto
    private MultipartFile[] imageFiles; //gpt
    //private List<MultipartFile> boardImgList = new ArrayList<>();


}
