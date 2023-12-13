package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReplyRepository extends JpaRepository<Review_Reply,Long> {
    public Long countByReview_ReviewId(Long reviewId);
    public Page<Review_Reply> findByReviewReviewIdIs(Long review_reviewId, Pageable pageable);
}


