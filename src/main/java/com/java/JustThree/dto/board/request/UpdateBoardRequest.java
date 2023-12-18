package com.java.JustThree.dto.board.request;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.domain.Users;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardRequest {
    private Long boardId;
    private String title;
    private String content;
    //private long viewCount;
    //private LocalDateTime created;
    //private LocalDateTime updated;
    private int noticeYn;
    private Users users;
    //save image request dto
    private MultipartFile[] imageFiles;
    //기존 이미지 수정 시
    private List<Long> imageIdList;

}
