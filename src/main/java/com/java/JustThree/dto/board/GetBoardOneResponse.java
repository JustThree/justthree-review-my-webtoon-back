package com.java.JustThree.dto.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.domain.Users;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardOneResponse {
    private Long boardId;
    private String title;
    private String content;
    private long viewCount;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int noticeYn;
    //작성자
    //private Users users;
    private String userNickname;
    private String userEmail;
    //첨부파일
    //private List<String> originNameList;
    //private List<String> accessUrlList;
    private List<Map<String, String>> boardImgMapList;

    //Entity -> DTO
    public static GetBoardOneResponse entityToDTO(Board board, List<BoardImage> boardImageList){
        List<Map<String, String>> fileMapList = new ArrayList<>();
        for(BoardImage boardImage : boardImageList){
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put(boardImage.getOriginName(), boardImage.getAccessUrl());
            fileMapList.add(fileMap);
        }

        return GetBoardOneResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .created(board.getCreated())
                .updated(board.getUpdated())
                .noticeYn(board.getNoticeYn())
                .userEmail(board.getUsers().getUsersEmail())
                .userNickname(board.getUsers().getUsersNickname())
                .boardImgMapList(fileMapList)
                .build();
    }
}
