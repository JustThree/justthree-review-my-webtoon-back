package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReplyRepository extends JpaRepository<Review_Reply,Long> {
    public Long countByReview_ReviewId(Long reviewId);
}


