package com.java.JustThree.dto.board.response;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardReply;
import com.java.JustThree.service.board.BoardReplyService;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardListResponse {
    private Long boardId;
    private String title;
    private String content;
    private long viewCount;
    private String created;
    private String updated;
    private int noticeYn;
    //작성자
    private Long usersId;
    private String userNickname;
    private String userEmail;
    //댓글 수
    private int boardReplyCount;

    //Entity → DTO
    public static GetBoardListResponse entityToDTO(Board board){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedCreated = board.getCreated().format(formatter);
        String formattedUpdated = board.getUpdated().format(formatter);

       // int cnt = boardReplyService.getBoardReplyList(board.getBoardId()).size()

        return GetBoardListResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .created(formattedCreated)
                .updated(formattedUpdated)
                .noticeYn(board.getNoticeYn())
                .usersId(board.getUsers().getUsersId())
                .userEmail(board.getUsers().getUsersEmail())
                .userNickname(board.getUsers().getUsersNickname())
                .boardReplyCount(board.getReplyCount())
                .build();
    }
}
