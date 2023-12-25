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
    private int noticeYn;
    private MultipartFile[] imageFiles;
    //기존 이미지 수정 시
    private List<Long> imageIdList;

}
