package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Review_Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHeartRepository extends JpaRepository<Review_Heart,Long> {
    public Long countByReview_ReviewId(Long reviewId);
    //  리뷰아이디와 유저아이디가 일치하는 리뷰 좋아요 조회
    public Optional<Review_Heart> findByReview_ReviewIdIsAndUsers_UsersIdIs(Long review_reviewId, Long users_usersId);
    //  리뷰아이디와 유저아이디가 일치하는 리뷰 좋아요가 있는지 체크
    boolean existsByReview_ReviewIdIsAndUsers_UsersIdIs(Long review_reviewId, Long review_users_usersId);
}
