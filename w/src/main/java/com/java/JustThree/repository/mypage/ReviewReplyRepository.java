package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewReplyRepository extends JpaRepository<Review_Reply,Long> {

    @Query("SELECT count(rr) FROM Review_Reply rr " +
            "WHERE rr.review.reviewId = :reviewId AND (rr.remove IS NULL OR rr.remove != :remove)")
    Long countByReviewReviewIdIsAndNotRemoved(@Param("reviewId") Long reviewId, @Param("remove") Integer remove);
    //
    @Query("SELECT rr FROM Review_Reply rr " +
            "WHERE rr.review.reviewId = :reviewId AND " +
            "(rr.remove IS NULL OR rr.remove != :remove)" +
            "order by rr.reviewReplyId desc ")
    Page<Review_Reply> findByReviewReviewIdIsAndNotRemoved(@Param("reviewId") Long reviewId, @Param("remove") Integer remove, Pageable pageable);

    Optional<Review_Reply> findByReviewReplyIdIs(Long reviewReplyId);
}


