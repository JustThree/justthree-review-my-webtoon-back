package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon,Long> {
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryBeginDeDesc(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryEndDeDesc(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMasterIdDesc(String ageGradCdNm, String mainGenreCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByViewDesc(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNot(String ageGradCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotAndMainGenreCdNmIs(String ageGradCdNm, String mainGenreCdNm, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotAndTitleContaining(String ageGradCdNm, String title, Pageable pageable);
    Page<Webtoon> findByAgeGradCdNmIsNotAndOutlineContaining(String ageGradCdNm, String outline, Pageable pageable);
    @Query("select w1 from Webtoon w1 where w1.ageGradCdNm != :ageNm and w1 in (select w2 from Webtoon w2 where w2.pictrWritrNm like %:writer% or w2.sntncWritrNm like %:writer%)")
    Page<Webtoon> findByAgeGradCdNmIsNotAndWriterIs(@Param("ageNm") String ageGradCdNm, @Param("writer") String writer, Pageable pageable);}
