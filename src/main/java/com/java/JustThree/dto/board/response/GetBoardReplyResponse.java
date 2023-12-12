package com.java.JustThree.dto.board.response;

import com.java.JustThree.domain.BoardReply;
import com.java.JustThree.domain.Users;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardReplyResponse {
    private Long boardReplyId;
    private Long boardId;
    //private Users users;
    private String boardReplyContent; //댓글 내용
    private String replyCreated;
    private String replyUpdated;
    private long parentReplyId; //부모 댓글 아이디
    //댓글 작성자
    private Long replyUsersId;
    private String replyUserNickname;
    private String replyUserEmail;

    //Entity -> DTO
    public static GetBoardReplyResponse entityToDTO(BoardReply boardReply){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedCreated = boardReply.getCreated().format(formatter);
        String formattedUpdated = boardReply.getUpdated().format(formatter);

        return GetBoardReplyResponse.builder()
                .boardReplyId(boardReply.getBoardReplyId())
                .boardId(boardReply.getBoard().getBoardId())
                .replyUsersId(boardReply.getUsers().getUsersId())
                .replyUserEmail(boardReply.getUsers().getUsersEmail())
                .replyUserNickname(boardReply.getUsers().getUsersNickname())
                .boardReplyContent(boardReply.getBoardReplyContent())
                .replyCreated(formattedCreated)
                .replyUpdated(formattedUpdated)
                .parentReplyId(boardReply.getParentReplyId())
                .build();
    }
}
