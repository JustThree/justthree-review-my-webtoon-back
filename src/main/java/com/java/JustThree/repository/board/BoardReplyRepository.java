package com.java.JustThree.repository.board;

import com.java.JustThree.domain.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    List<BoardReply> findByBoard_BoardIdIsOrderByCreatedDesc(Long board_boardId);

}
