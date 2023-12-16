package com.java.JustThree.repository.board;

import com.java.JustThree.domain.BoardLike;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsBoardLikeByBoard_BoardIdAndUsers_UsersId(Long boardId, Long usersId);

    long countByBoard_BoardId(Long boardId);

}
