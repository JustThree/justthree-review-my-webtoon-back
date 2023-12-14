package com.java.JustThree.repository;

import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star,Long> {

    public List<Star> findByUsers_UsersId(Long usersId);

    public List<Star> findByUsers_UsersId_OrderByStarValDesc(Long usersId);
    public List<Star> findByUsers_UsersId_OrderByStarVal(Long usersId);

    public List<Star> findByUsers_UsersIdAndStarVal(Long usersId,int starVal);

    Long countByUsers_UsersId(Long usersId);

    // 웹툰에서 평가 수 모두 조회
    List<Star> findByWebtoon_MasterIdIs(Long webtoon_masterId);

    Optional<Star> findByWebtoon_MasterIdIsAndUsers_UsersIdIs(Long webtoon_masterId, Long users_usersId);

}
