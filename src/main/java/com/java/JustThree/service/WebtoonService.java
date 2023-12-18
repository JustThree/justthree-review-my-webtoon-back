package com.java.JustThree.service;

import com.java.JustThree.domain.*;
import com.java.JustThree.dto.main.response.*;
import com.java.JustThree.jwt.JwtProvider;
import com.java.JustThree.repository.ReviewRepository;
import com.java.JustThree.repository.StarRepository;
import com.java.JustThree.repository.UsersRepository;
import com.java.JustThree.repository.WebtoonRepository;
import com.java.JustThree.repository.mypage.InterestRepository;
import com.java.JustThree.repository.mypage.ReviewHeartRepository;
import com.java.JustThree.repository.mypage.ReviewReplyRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WebtoonService {
    @Value("${gujang}")
    private String gujang;
    final
    WebtoonRepository webtoonRepository;
    final StarRepository starRepository;
    final UsersRepository usersRepository;
    final ReviewRepository reviewRepository;
    final JwtProvider jwtProvider;
    final ReviewHeartRepository reviewHeartRepository;
    final ReviewReplyRepository reviewReplyRepository;
    final InterestRepository interestRepository;

    public WebtoonService(WebtoonRepository webtoonRepository, StarRepository starRepository, UsersRepository usersRepository, ReviewRepository reviewRepository, JwtProvider jwtProvider, ReviewHeartRepository reviewHeartRepository, ReviewReplyRepository reviewReplyRepository, InterestRepository interestRepository) {
        this.webtoonRepository = webtoonRepository;
        this.starRepository = starRepository;
        this.usersRepository = usersRepository;
        this.reviewRepository = reviewRepository;
        this.jwtProvider = jwtProvider;
        this.reviewHeartRepository = reviewHeartRepository;
        this.reviewReplyRepository = reviewReplyRepository;
        this.interestRepository = interestRepository;

    }
    // 페이지 단건 조회
    @Transactional
    public WebtoonDetailResponse getWebtoonDetail(String token,long id){
        long sum = 0L;
        int userStar = 5;
        float avg = 0f;
        boolean interested = false;

        // 게시글 내용 조회
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(IllegalAccessError::new);
        // 토큰으로 유저아이디 조회
        if (token != null) {
            Optional<Star> byWebtoonMasterIdIsAndUsersUsersIdIs = starRepository.findByWebtoon_MasterIdIsAndUsers_UsersIdIs(id, jwtProvider.getUserId(token));
            if (byWebtoonMasterIdIsAndUsersUsersIdIs.isPresent()){
                userStar = byWebtoonMasterIdIsAndUsersUsersIdIs.get().getStarVal();
            }
        }
        // 별점 매긴 사람 숫자 조회
        List<Star> byWebtoonMasterIdIs = starRepository.findByWebtoon_MasterIdIs(id);
        for (Star star :byWebtoonMasterIdIs)
        {
            sum += star.getStarVal();
        }
        if (!byWebtoonMasterIdIs.isEmpty()) {
            avg = (float) sum / byWebtoonMasterIdIs.size();
        }
        if (webtoon.getView()==null){
            webtoon.setView(1L);
        }else {
            webtoon.setView(webtoon.getView() + 1);
        }
        if (token != null){
            Optional<Interest> byUsersUsersIdIs = interestRepository.findByUsers_UsersIdIsAndWebtoon_MasterIdIs(jwtProvider.getUserId(token), webtoon.getMasterId());
            if (byUsersUsersIdIs.isPresent()){
                interested = true;
            }
        }
        return WebtoonDetailResponse.fromEntity(webtoon,
                avg,
                byWebtoonMasterIdIs.size(),
                userStar,
                interested,
                webtoon.getAgeGradCdNm().equals("19세 이상"));
    }
    // webtoon 전체 조회
    public Page<WebtoonMainResponse> getWebtoonPage(Pageable pageable, String genre, String order){
        Page<WebtoonMainResponse> webtoonMainResponsePage;
        String orderVal;
        switch (order){
            case "latest" :  orderVal = "masterId";
            break;
            case "like": orderVal = "view";
                break;
            case "rate": orderVal = "masterId";
                break;
            default : orderVal = "masterId";
                break;
        }

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,
                orderVal));
        if (!order.equals("rate")) {
            webtoonMainResponsePage = switch (genre) {
                case "fantasy" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                                ("19세 이상", "판타지", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "romance" -> webtoonRepository.findByAgeGradCdNmIsNotAndDoubleGenreIs
                                ("19세 이상", "이성애", "로맨스", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "school" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                                ("19세 이상", "학원", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "daily" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                                ("19세 이상", "일상", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "comic" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                                ("19세 이상", "코믹", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "martialarts" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIs
                                ("19세 이상", "무협", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                default -> webtoonRepository.findByAgeGradCdNmIsNot
                                ("19세 이상", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
            };
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            webtoonMainResponsePage = switch (genre) {
                case "fantasy" -> webtoonRepository.findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity
                                ("19세 이상", "판타지", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "romance" -> webtoonRepository.findByAgeGradCdNmIsNotAndTripleGenreIsOrderByPopularity
                                ("19세 이상", "이성애", "로맨스","순정", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "school" -> webtoonRepository.findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity
                                ("19세 이상", "학원", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "daily" -> webtoonRepository.findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity
                                ("19세 이상", "일상", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "comic" -> webtoonRepository.findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity
                                ("19세 이상", "코믹", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                case "martialarts" -> webtoonRepository.findByAgeGradCdNmIsNotAndGenreIsOrderByPopularity
                                ("19세 이상", "무협", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
                default -> webtoonRepository.findByAgeGradCdNmIsNotAndOrderByPopularity
                                ("19세 이상", pageable)
                        .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));

            };
        }
        return  webtoonMainResponsePage;
    }
    // 웹툰 키워드로 리스트 조회
    public List<WebtoonMainResponse> getWebtoonKeyword(Pageable pageable,String keyword){
        List<WebtoonMainResponse> webtoonMainResponseList = null;
        webtoonMainResponseList = switch (keyword) {
            case "recent" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByPusryBeginDeDesc
                            ("19세 이상", pageable)
                    .stream()
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId()))).toList();
            case "recentend" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByPusryEndDeDesc
                            ("19세 이상", pageable)
                    .stream()
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId()))).toList();
            case "fantasy" -> webtoonRepository.findByAgeGradCdNmIsNotAndMainGenreCdNmIsOrderByMasterIdDesc
                            ("19세 이상", "판타지", pageable)
                    .stream()
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId()))).toList();
            case "love" -> webtoonRepository.findByAgeGradCdNmIsNotAndTripleGenreIsOrderByPopularity
                            ("19세 이상", "이성애", "로맨스","순정",pageable)
                    .stream()
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId()))).toList();
            case "famous" -> webtoonRepository.findByAgeGradCdNmIsNotOrderByViewDesc
                            ("19세 이상", pageable)
                    .stream()
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId()))).toList();
            default -> webtoonMainResponseList;
        };
        return webtoonMainResponseList;
    }


    public Page<WebtoonMainResponse> searchWebtoon(Pageable pageable, String type, String word) {
        Page<WebtoonMainResponse> webtoonMainResponsePage;
        if (type.equals("user")) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,
                    "usersNickname"));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,
                    "title"));
        }
        webtoonMainResponsePage = switch (type) {
            case "title" -> webtoonRepository.findByAgeGradCdNmIsNotAndTitleContaining
                    ("19세 이상",word,pageable)
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));

            case "outline" -> webtoonRepository.findByAgeGradCdNmIsNotAndOutlineContaining
                            ("19세 이상",word,pageable)
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));

            case "writer" -> webtoonRepository.findByAgeGradCdNmIsNotAndWriterIs
                            ("19세 이상",word,pageable)
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));
            case "user" -> usersRepository.findByUsersNicknameContaining
                    (word,pageable)
                    .map(WebtoonMainResponse::fromEntity);
            default -> webtoonRepository.findByAgeGradCdNmIsNotAndTitleContaining
                            ("19세 이상",word,pageable)
                    .map((webtoon) -> WebtoonMainResponse.fromEntity(webtoon,starRepository.getAverageRatingForMasterId(webtoon.getMasterId())));

        };
        System.out.println(1);
        return  webtoonMainResponsePage;

    }
    @Transactional
    public void ratingWebtoon(String token,Long masterId,Integer rating){
        // 있는지 확인
        Long userId = jwtProvider.getUserId(token);
        Optional<Star> byWebtoonMasterIdIsAndUsersUsersIdIs = starRepository.findByWebtoon_MasterIdIsAndUsers_UsersIdIs(masterId, userId);
        if (byWebtoonMasterIdIsAndUsersUsersIdIs.isPresent()) {
            byWebtoonMasterIdIsAndUsersUsersIdIs.get().setStarVal(rating);
            // 있으면 수정
        } else {
            // 없으면 저장
            starRepository.save(Star.builder()
                    .users(usersRepository.findById(userId).orElseThrow(IllegalArgumentException::new))
                    .webtoon(webtoonRepository.findById(masterId).orElseThrow(IllegalAccessError::new))
                    .starVal(rating)
                    .build());
        }
    }
    public Page<WebtoonDetailReviewResponse> getWebtoonReviewsPage(Long masterId,Pageable pageable){
        return reviewRepository.findByWebtoon_MasterIdIsAndRemoveIsNot(masterId, 1,pageable).
                map((review -> WebtoonDetailReviewResponse.fromEntity(review,
                        starRepository.findByWebtoon_MasterIdIsAndUsers_UsersIdIs(
                                masterId,review.getUsers().getUsersId())
                                .orElse(Star.builder().starVal(0).build()).getStarVal(),
                        reviewHeartRepository.countByReview_ReviewId(review.getReviewId()),
                        reviewReplyRepository.countByReviewReviewIdIsAndNotRemoved(review.getReviewId(),1)
                )));
    }
    @Transactional
    public String modifyInterest(String token, Long masterId){
        if (token != null) {
            Long userId = jwtProvider.getUserId(token);
            Optional<Interest> byUsers_usersIdIsAndWebtoon_masterIdIs = interestRepository.findByUsers_UsersIdIsAndWebtoon_MasterIdIs(userId, masterId);
            if (byUsers_usersIdIsAndWebtoon_masterIdIs.isPresent()){
                interestRepository.delete(byUsers_usersIdIsAndWebtoon_masterIdIs.get());
                return "관심 삭제 되었어요.";
            } else {
                interestRepository.save(Interest.builder()
                        .webtoon(webtoonRepository.findById(masterId).orElseThrow(() -> new IllegalArgumentException("웹툰 값이 잘못 입력됬습니다.")))
                        .users(usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저값이 잘못 됬습니다.")))
                        .build());
                return "관심 등록 완료!";
            }
        }
        return "로그인이 안되어 있어요.";
    }
    @Transactional
    public void writeReview(String token,Long masterId,String content){
        reviewRepository.save(Review.builder()
                .webtoon(webtoonRepository.findById(masterId).orElseThrow(() -> new IllegalArgumentException("")))
                .users(usersRepository.findById(jwtProvider.getUserId(token)).orElseThrow(()-> new IllegalArgumentException("")))
                .content(content)
                .build()
        );
    }
    public ReviewDetailResponse getReview(Long reviewId, String token){
        boolean checkLike = false;
        Review review = reviewRepository.findById(reviewId).orElseThrow(()
                -> new IllegalArgumentException("해당 리뷰가 없어요"));
        if (token != null) {
            if (reviewHeartRepository.existsByReview_ReviewIdIsAndReview_Users_UsersIdIs(reviewId,
                    jwtProvider.getUserId(token))) {
                checkLike = true;
            }
        }
        return ReviewDetailResponse.fromEntity(
                review, starRepository.findByWebtoon_MasterIdIsAndUsers_UsersIdIs(
                                review.getWebtoon().getMasterId(),review.getUsers().getUsersId())
                        .orElse(Star.builder().starVal(0).build()), !(review.getRemove() == null || review.getRemove() == 0)
                ,checkLike);}

    public Page<ReviewReplyResponse> getReviewReplyResponse(Pageable pageable,Long reviewId){
        return reviewReplyRepository.findByReviewReviewIdIsAndNotRemoved(reviewId,1,pageable).map(ReviewReplyResponse::fromEntity);
    }
    @Transactional
    public String  modifyReviewLike(Long reviewId, String token){
        Optional<Review_Heart> byReviewReviewIdIs = reviewHeartRepository.findByReview_ReviewIdIs(reviewId);
        if (byReviewReviewIdIs.isPresent()){
            reviewHeartRepository.delete(byReviewReviewIdIs.get());
            return "좋아요가 삭제되었어요";
        } else {
            reviewHeartRepository.save(
                    Review_Heart.builder()
                            .users(usersRepository.findById(jwtProvider.getUserId(token)).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 토큰")))
                            .review(reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 잘못됬어요!")))
                .build());
            return "좋아요 버튼 누르셨어요!";
        }

    }
    @Transactional
    public String addReviewReply(Long reviewId, String token, String content){
        reviewReplyRepository.save(
                    Review_Reply.builder()
                            .review(reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 잘못됬어요")))
                            .users(usersRepository.findById(jwtProvider.getUserId(token)).orElseThrow(() -> new IllegalArgumentException("토큰이 잘못됫어요")))
                            .content(content)
                            .build());
        
        return "저장 완료";
    }
    @Transactional
    public void removeReview(Long reviewId,String token) throws Exception{
        if (jwtProvider.getUserId(token).equals(reviewRepository.findById(reviewId).get().getUsers().getUsersId())) {
        Optional<Review> byReviewIdIsAndUsersUsersIdIs = reviewRepository.findByReviewIdIsAndUsers_UsersIdIs(reviewId, jwtProvider.getUserId(token));
        byReviewIdIsAndUsersUsersIdIs.get().setRemove(1);
        } else {
            throw new Exception("잘못된 요청입니다.");
        }
    }
    @Transactional
    public void fixReview(Long reviewId,String token,String content) throws Exception {
        if (jwtProvider.getUserId(token).equals(reviewRepository.findById(reviewId).get().getUsers().getUsersId())) {
            Optional<Review> byReviewIdIsAndUsersUsersIdIs = reviewRepository.findByReviewIdIsAndUsers_UsersIdIs(reviewId, jwtProvider.getUserId(token));
            byReviewIdIsAndUsersUsersIdIs.ifPresent(review -> review.setContent(content));
        } else {
            throw new Exception("잘못된 요청입니다.");
        }
    }
    @Transactional
    public void removeReviewReply(Long reviewReplyId,String token) throws Exception{
        if (jwtProvider.getUserId(token).equals(reviewReplyRepository.findByReviewReplyIdIs(reviewReplyId).get().getUsers().getUsersId())) {
            Optional<Review_Reply> byReviewReplyIdIs = reviewReplyRepository.findByReviewReplyIdIs(reviewReplyId);
            byReviewReplyIdIs.get().setRemove(1);
        } else {
            throw new Exception("잘못된 요청입니다.");
        }
    }


    @Transactional
    public void fixReviewReply(Long reviewReplyId,String token,String content) throws Exception {
        if (jwtProvider.getUserId(token).equals(reviewReplyRepository.findByReviewReplyIdIs(reviewReplyId).get().getUsers().getUsersId())) {// 아이디 같은지 체크
            Optional<Review_Reply> byReviewReplyIdIs = reviewReplyRepository.findByReviewReplyIdIs(reviewReplyId); // 조회
            byReviewReplyIdIs.ifPresent(reviewReply -> reviewReply.setContent(content)); // 수정
        } else {
            throw new Exception("잘못된 요청입니다.");
        }
    }
    // 웹툰 초기화
    public void webtoonInit(Map<String, Webtoon> mapJson, Set<String> setNotNormal, int idx) {
        List<Webtoon> webtoons = new ArrayList<>();
        HttpURLConnection conn = null;
        String viewItemCntVal = "100";
        String listSeCdVal = "1"; // 1  :  웹툰 2  :  도서(만화책) 3  :  잡지 4 :  영화 5  :  드라마 6  :  게임 7 :  공연,전시 8  :  행사(전시,행사,축제,컨퍼런스,공모전) 9  :  상품
        String prvKeyVal = gujang; // properties 적기
        String page = String.valueOf(idx);// 페이지를 viewItemCntVal 씩 늘려야댐..
        try {
            String openApiUrl = "https://kmas.or.kr/openapi/search/rgDtaMasterList";
            openApiUrl += "?";
            openApiUrl += "prvKey" + "=" + prvKeyVal + "&";
            openApiUrl += "listSeCd" + "=" + listSeCdVal + "&";
            openApiUrl += "viewItemCnt" + "=" + viewItemCntVal + "&";
            openApiUrl += "pageNo" + "=" + page;
            URL url = new URL(openApiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            // Read the response into a StringBuilder
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            try {
                jsonObject = new JSONObject(response.toString());
                jsonArray = jsonObject.getJSONArray("itemList");
            } catch (Exception e) {
                System.out.println(jsonObject);
                throw new RuntimeException(e);
            }
            conn.disconnect();
            for (Object ele :
                    jsonArray) {
                JSONObject js = new JSONObject(ele.toString());
                // 중복 만화 체크 키
                String tittlePicWriSnWri = js.get("title").toString() + "_" + js.get("pictrWritrNm").toString() + "_" + js.get("sntncWritrNm").toString();
                String 규장각Url = "https://www.kmas.or.kr/archive/book/" + js.get("mastrId");
                String link = "";
                String newLink = "";
                // 필터
                if (js.get("ageGradCdNm").equals("19세 이상") || js.get("ageGradCdNm").equals("확인필요")// 함수화해서 리팩토링 하기
                        || js.get("pltfomCdNm").equals("레알코믹스") || js.get("pltfomCdNm").equals("셀툰") || js.get("pltfomCdNm").equals("야툰")
                        || js.get("title").toString().contains("에로")||js.get("title").toString().contains("개정판") || js.get("title").toString().contains("섹스") || js.get("title").toString().contains("섹기") || js.get("title").toString().contains("섹파") ||js.get("title").toString().contains("색툰")|| js.get("title").toString().contains("야썰")
                        || js.get("mainGenreCdNm").equals("동성애") || js.get("mainGenreCdNm").equals("BL") || js.get("mainGenreCdNm").equals("GL")
                        || js.get("outline").toString().contains("에로")||js.get("outline").toString().contains("개정판") || js.get("outline").toString().contains("섹스") || js.get("outline").toString().contains("섹기") || js.get("outline").toString().contains("섹파") ||js.get("outline").toString().contains("색툰")|| js.get("outline").toString().contains("야썰")) {
                    // 19세인경우 set에 추가
                    setNotNormal.add(tittlePicWriSnWri);
                } else {
                    // 아니면 jsoup연결후 주소 가져오기
                    Connection jsoupConn = Jsoup.connect(규장각Url);
                    Document document = jsoupConn.get();
                    Elements elements = document.getElementsByClass("dv-table w100p vd-table");
                    Elements aTags = elements.select("a");
                    String[] split = aTags.toString().split(" ");
                    for (String s :
                            split) {
                        if (s.contains("http")) {
                            int front = s.indexOf("'");
                            int back = s.lastIndexOf("'");
                            link = s.substring(front + 1, back);
                            break;
                        }
                    }
                }

                if (mapJson.containsKey(tittlePicWriSnWri)) {
                    String oldLink = mapJson.get(tittlePicWriSnWri).getUrls();
                    newLink = oldLink + "+" + js.get("pltfomCdNm") + "$" + link;
                    Optional<Webtoon> byId = webtoonRepository.findById(mapJson.get(tittlePicWriSnWri).getMasterId());
                    if (byId.isPresent()) {
                        Webtoon webtoon = byId.get();
                        webtoon.setUrls(newLink);
                        webtoons.add(webtoon);
                    } else {
                    }
                } else {
                    newLink = js.get("pltfomCdNm") + "$" + link;
                    Webtoon webtoon = Webtoon.builder()
                            .masterId(Long.parseLong(js.get("mastrId").toString()))
                            .title(js.get("title").toString())
                            .pictrWritrNm(js.get("pictrWritrNm").toString())
                            .sntncWritrNm(js.get("sntncWritrNm").toString())
                            .mainGenreCdNm(js.get("mainGenreCdNm").toString())
                            .outline(js.get("outline").toString())
                            .pltfomCdNm(js.get("pltfomCdNm").toString())
                            .ageGradCd(js.get("ageGradCd").toString())
                            .ageGradCdNm(js.get("ageGradCdNm").toString())
                            .pusryBeginDe(js.get("pusryBeginDe").toString())
                            .pusryEndDe(js.get("pusryEndDe").toString())
                            .fnshYn(js.get("fnshYn").toString())
                            .webtoonPusryYn(js.get("webtoonPusryYn").toString())
                            .orginlNationCdNm(js.get("orginlNationCdNm").toString())
                            .urls(newLink) // 성인일 경우 url ... 로그인 해야댐.. 나중에 url 넣기
                            .imageUrl(js.get("imageDownloadUrl").toString())
                            .build();
                    webtoons.add(webtoon);
                    mapJson.put(tittlePicWriSnWri, webtoon);
                }
                // 엔티티 생성

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        for (Webtoon webtoon:
             webtoons) {
            //js.get("title").toString() + "_" + js.get("pictrWritrNm").toString() + "_" + js.get("sntncWritrNm").toString()
            if (setNotNormal.contains(webtoon.getTitle() + "_" + webtoon.getPictrWritrNm() + "_" + webtoon.getSntncWritrNm())){
                Optional<Webtoon> byId = webtoonRepository.findById(webtoon.getMasterId());
            if (byId.isPresent()) {
                byId.get().setAgeGradCd("4");
                byId.get().setAgeGradCdNm("19세 이상");
            }
        }

        webtoonRepository.saveAll(webtoons);
        }
    }

}