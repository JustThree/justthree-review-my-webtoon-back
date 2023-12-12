package com.java.JustThree.dto.board.response;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardOneResponse {
    private Long boardId;
    private String title;
    private String content;
    private long viewCount;
    private String created;
    private String updated;
    private int noticeYn;
    //작성자
    private String userNickname;
    private String userEmail;
    //첨부파일
    //private List<Map<String, String>> boardImgMapList;
    private List<ImageDataResponse> boardImgMapList;
    //댓글
    private List<GetBoardReplyResponse> boardReplyList;
    //좋아요


    //Entity -> DTO
    public static GetBoardOneResponse entityToDTO(Board board,
                                                  List<BoardImage> boardImageList,
                                                  List<GetBoardReplyResponse> boardReplyList){
        /*List<Map<String, String>> fileMapList = new ArrayList<>();
        for(BoardImage boardImage : boardImageList){
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put(boardImage.getOriginName(), boardImage.getAccessUrl());
            fileMapList.add(fileMap);
        }*/
        List<ImageDataResponse> fileMapList = new ArrayList<>();
        for(BoardImage boardImage : boardImageList){
            ImageDataResponse imageData = new ImageDataResponse(boardImage.getImgId(), boardImage.getAccessUrl(), boardImage.getOriginName());
            fileMapList.add(imageData);
        }
        System.out.println(board.getCreated());
        System.out.println(board.getUpdated());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedCreated = board.getCreated().format(formatter);
        String formattedUpdated = board.getUpdated().format(formatter);
        System.out.println(formattedCreated);
        System.out.println(formattedUpdated);

        return GetBoardOneResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .created(formattedCreated)
                .updated(formattedUpdated)
                .noticeYn(board.getNoticeYn())
                .userEmail(board.getUsers().getUsersEmail())
                .userNickname(board.getUsers().getUsersNickname())
                .boardImgMapList(fileMapList)
                .boardReplyList(boardReplyList)
                .build();
    }
}
