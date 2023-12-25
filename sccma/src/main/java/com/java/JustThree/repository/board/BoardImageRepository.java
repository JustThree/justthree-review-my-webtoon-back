package com.java.JustThree.repository.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    List<BoardImage> findByBoard(Board b);
    List<BoardImage> findByBoard_BoardIdIs(Long board_boardId);

}
