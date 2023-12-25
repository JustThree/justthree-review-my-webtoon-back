package com.java.JustThree.dto.board.request;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.domain.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardRequest {
    private String title;
    private String content;
    private int noticeYn;
    private MultipartFile[] imageFiles;
}
