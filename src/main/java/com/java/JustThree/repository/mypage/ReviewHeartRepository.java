package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHeartRepository extends JpaRepository<Review_Heart,Long> {
    public Long countByReview_ReviewId(Long reviewId);


}
