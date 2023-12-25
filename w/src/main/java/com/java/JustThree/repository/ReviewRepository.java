package com.java.JustThree.repository;

import com.java.JustThree.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    public List<Review> findByUsers_UsersIdIs(Long users_usersId);
    public Long countByUsers_UsersId(Long usersID);
    @Query("SELECT r FROM Review r " +
            "WHERE r.webtoon.masterId = :webtoon_masterId  " +
            "AND (r.remove IS NULL OR r.remove != :remove)" +
            "ORDER BY r.reviewId desc")
    Page<Review> findByWebtoon_MasterIdIsAndRemoveIsNot(@Param("webtoon_masterId") Long webtoon_masterId,@Param("remove") Integer remove, Pageable pageable);

    Optional<Review> findByReviewIdIsAndUsers_UsersIdIs(Long reviewId, Long users_usersId);
}
