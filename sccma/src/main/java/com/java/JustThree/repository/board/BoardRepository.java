package com.java.JustThree.repository.board;

import com.java.JustThree.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Specification<Board> specification, Pageable pageable);

    //공지사항 전체 목록
    List<Board> findAllByNoticeYnOrderByCreatedDesc(int isNotice);

    List<Board> findByNoticeYnAndTitleContainingOrNoticeYnAndContentContaining(int noticeYn1, String keyword1, int noticeYn2, String keyword2);


    //조회수 증가
    @Modifying
    @Query("update Board  b set b.viewCount = :viewCount where b.boardId = :id")
    void updateViewCount(@Param("viewCount") int viewCount, @Param("id") Long id);
}

