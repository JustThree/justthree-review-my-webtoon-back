package com.java.JustThree.repository.board;

import com.java.JustThree.domain.BoardLike;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    //게시글 좋아요 삭제 관련
    BoardLike findBoardLikeByBoard_BoardIdAndUsers_UsersId(Long boardId, Long usersId);
    
    //게시글 좋아요 여부
    boolean existsBoardLikeByBoard_BoardIdAndUsers_UsersId(Long boardId, Long usersId);

    //게시글 좋아요 개수
    long countByBoard_BoardId(Long boardId);

}
