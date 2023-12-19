package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHeartRepository extends JpaRepository<Review_Heart,Long> {
    public Long countByReview_ReviewId(Long reviewId);

    public Optional<Review_Heart> findByReview_ReviewIdIsAndUsers_UsersIdIs(Long review_reviewId, Long users_usersId);

    boolean existsByReview_ReviewIdIsAndUsers_UsersIdIs(Long review_reviewId, Long review_users_usersId);
}
