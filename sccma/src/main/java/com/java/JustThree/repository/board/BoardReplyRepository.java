package com.java.JustThree.repository.board;

import com.java.JustThree.domain.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    List<BoardReply> findByBoard_BoardIdIsOrderByCreatedDesc(Long board_boardId);

    //parentId가 boardId인 댓글(댓글 삭제 시 대댓글 같이 삭제)
    List<BoardReply> findByParentReplyId(Long id);

}
