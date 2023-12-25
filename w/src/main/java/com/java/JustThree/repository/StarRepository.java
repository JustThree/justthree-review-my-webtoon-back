package com.java.JustThree.repository;

import com.java.JustThree.domain.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star,Long> {
    // 웹툰에서 평가 수 모두 조회
    List<Star> findByWebtoon_MasterIdIs(Long webtoon_masterId);
    // 자기가 해당 웹툰에 평가한 별점 조회
    Optional<Star> findByWebtoon_MasterIdIsAndUsers_UsersIdIs(Long webtoon_masterId, Long users_usersId);
    // 해당 웹툰 평균 별점 조회
    @Query("SELECT AVG(s.starVal) FROM Star s WHERE s.webtoon.masterId = :masterId")
    Float getAverageRatingForMasterId(@Param("masterId") Long masterId);

}
