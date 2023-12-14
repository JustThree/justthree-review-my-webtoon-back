package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHeartRepository extends JpaRepository<Review_Heart,Long> {
    public Long countByReview_ReviewId(Long reviewId);

    public Optional<Review_Heart> findByReview_ReviewIdIs(Long review_reviewId);


}
