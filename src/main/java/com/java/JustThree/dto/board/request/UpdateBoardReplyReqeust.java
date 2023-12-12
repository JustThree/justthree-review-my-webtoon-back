package com.java.JustThree.dto.board.request;

import com.java.JustThree.domain.Users;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardReplyReqeust {
    private Long boardReplyId;
    private Long boardId;
    private Users users;
    private String boardReplyContent; //댓글 내용
    private long parentReplyId; //부모 댓글 아이디
}
