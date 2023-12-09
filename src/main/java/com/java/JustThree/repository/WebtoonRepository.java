package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon,Long> {
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryBeginDeDesc(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryEndDeDesc(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMastrIdDesc(String ageGradCdNm, String mainGenreCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByViewDesc(String ageGradCdNm, Pageable pageable);
}
