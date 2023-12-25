package com.java.JustThree.repository;


import com.java.JustThree.domain.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon,Long> {
    //AgeGradCdNmIsNot <= 성인 웹툰 인지 아닌지 조회하는것.
    // 메인 페이지 용 최근 연재 시작된 웹툰
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryBeginDeDesc(String ageGradCdNm, Pageable pageable);
    // 메인 페이지 용 최근 연재 끝난 웹툰
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByPusryEndDeDesc(String ageGradCdNm, Pageable pageable);
    // 메인 페이지 용 장르이름이 일치하는 웹툰들
    Page<Webtoon> findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMasterIdDesc(String ageGradCdNm, String mainGenreCdNm, Pageable pageable);
    // 조회순 웹툰 정렬
    Page<Webtoon> findByAgeGradCdNmIsNotOrderByViewDesc(String ageGradCdNm, Pageable pageable);
    // 모든 웹툰 조회
    Page<Webtoon> findByAgeGradCdNmIsNot(String ageGradCdNm, Pageable pageable);
    // 제목 검색
    Page<Webtoon> findByAgeGradCdNmIsNotAndTitleContaining(String ageGradCdNm, String title, Pageable pageable);
    // 줄거리 검색
    Page<Webtoon> findByAgeGradCdNmIsNotAndOutlineContaining(String ageGradCdNm, String outline, Pageable pageable);
    // 작가 이름 검색
    @Query("select w1 " +
            "from Webtoon w1 " +
            "where w1.ageGradCdNm != :ageNm and w1 " +
            "in (select w2 from Webtoon w2 where w2.pictrWritrNm like %:writer% or w2.sntncWritrNm like %:writer%)")
    Page<Webtoon> findByAgeGradCdNmIsNotAndWriterIs(@Param("ageNm") String ageGradCdNm, @Param("writer") String writer, Pageable pageable);
    //장르 1개 조회
    Page<Webtoon> findByAgeGradCdNmIsNotAndMainGenreCdNmIs(String ageGradCdNm, String mainGenreCdNm, Pageable pageable);
    // 장르 2개 조회
    @Query("select w1 " +
            "from Webtoon w1" +
            " where w1.ageGradCdNm != :ageNm " +
            "and w1 in " +
            "(select w2 " +
            "from Webtoon w2 " +
            "where w2.mainGenreCdNm = :genre1 or w2.mainGenreCdNm = :genre2)")
    Page<Webtoon> findByAgeGradCdNmIsNotAndDoubleGenreIs(@Param("ageNm") String ageNm, @Param("genre1") String genre1, @Param("genre2") String genre2, Pageable pageable);
    // 전체 조회
    @Query("SELECT w " +
            " FROM Webtoon w " +
            "LEFT JOIN Star s " +
            "on s.webtoon.masterId = w.masterId " +
            "WHERE w.ageGradCdNm != :ageNm " +
            "GROUP BY w.masterId, s.webtoon.masterId" +
            " ORDER BY COALESCE(AVG(s.starVal), 0) DESC")
    Page<Webtoon> findByAgeGradCdNmIsNotAndOrderByPopularity(@Param("ageNm") String ageNm, Pageable pageable);

    // 장르 1개 별점순 조회
    @Query("SELECT w " +
            "FROM Webtoon w " +
            "LEFT JOIN Star s " +
            "on s.webtoon.masterId = w.masterId " +
            "WHERE w.mainGenreCdNm = :genre1 " +
            "AND w.ageGradCdNm != :ageNm " +
            "GROUP BY w.masterId, s.webtoon.masterId " +
            "ORDER BY COALESCE(AVG(s.starVal), 0) DESC")
    Page<Webtoon> findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity(@Param("ageNm") String ageNm, @Param("genre1") String genre1,Pageable pageable);
    // 장르 2개 별점순 조회
    @Query("SELECT w " +
            "FROM Webtoon w " +
            "LEFT JOIN Star s " +
            "on s.webtoon.masterId = w.masterId " +
            "WHERE (w.mainGenreCdNm = :genre1 OR w.mainGenreCdNm = :genre2) " +
            "AND w.ageGradCdNm != :ageNm " +
            "GROUP BY w.masterId, w.title " +
            "ORDER BY COALESCE(AVG(s.starVal), 0) DESC")
    Page<Webtoon> findByAgeGradCdNmIsNotAndDoubleGenreIsOrderByPopularity(@Param("ageNm") String ageNm, @Param("genre1") String genre1, @Param("genre2") String genre2,Pageable pageable);

    // 장르 3개 별점순 조회
    @Query("SELECT w " +
            "FROM Webtoon w " +
            "LEFT JOIN Star s " +
            "on s.webtoon.masterId = w.masterId " +
            "WHERE (w.mainGenreCdNm = :genre1 OR w.mainGenreCdNm = :genre2 OR w.mainGenreCdNm = :genre3) " +
            "AND w.ageGradCdNm != :ageNm " +
            "GROUP BY w.masterId, w.title " +
            "ORDER BY COALESCE(AVG(s.starVal), 0) DESC")
    Page<Webtoon> findByAgeGradCdNmIsNotAndTripleGenreIsOrderByPopularity(@Param("ageNm") String ageNm, @Param("genre1") String genre1, @Param("genre2") String genre2,@Param("genre3") String genre3,Pageable pageable);

}