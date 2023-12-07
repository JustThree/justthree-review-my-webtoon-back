package com.java.JustThree.repository;

import com.java.JustThree.domain.Review_Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReplyRepository extends JpaRepository<Review_Reply,Long> {
    public Long countByReviewReplyId (Long reviewReplyId);

}
