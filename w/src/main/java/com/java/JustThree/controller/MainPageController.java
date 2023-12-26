package com.java.JustThree.controller;

import com.java.JustThree.dto.main.request.AddReviewReplyRequest;
import com.java.JustThree.dto.main.request.ModifyReviewReplyRequest;
import com.java.JustThree.dto.main.request.ModifyReviewRequest;
import com.java.JustThree.dto.main.request.PostWebtoonReviewRequest;
import com.java.JustThree.service.WebtoonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/webtoon")
@RestController
@AllArgsConstructor
public class MainPageController {
    WebtoonService webtoonService;

    // 웹툰 단건 조회 페이지
    @GetMapping("/{id}")
    public ResponseEntity<?> webtoonDetail(
            @RequestHeader(value = "Authorization",required = false) String token, // 즐겨찾기 했는지 조회를 위한 토큰
            @PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonDetail(token, id));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .notFound()
                    .header("error", e.getMessage())
                    .build();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 메인 페이지 슬라이드용 api
    @GetMapping("/webtoonlist")
    public ResponseEntity<?> webtoonKeywordList(@PageableDefault(size = 25) Pageable pageable, // 기본 페이지 사이즈 25
                                                @RequestParam(name = "keyword") String keyword) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonKeyword(pageable, keyword));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 웹툰 전체 조회 페이징 api
    @GetMapping("")
    public ResponseEntity<?> webtoonList(@PageableDefault(size = 24) Pageable pageable, @RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "order", required = false) String order) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonPage(pageable, genre, order));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }

    }
    // 웹툰 검색 api
    @GetMapping("/search")
    public ResponseEntity<?> searchList(@PageableDefault(size = 24) Pageable pageable, @RequestParam(name = "type") String type, @RequestParam(name = "word") String word) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.searchWebtoon(pageable, type, word));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // masterId인 웹툰 리뷰들 조회
    @GetMapping("/reviews/{masterId}")
    public ResponseEntity<?> reviewsWebtoon(@PathVariable(name = "masterId") Long masterId, @PageableDefault(size = 12) Pageable pageable) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonReviewsPage(masterId, pageable));
        } catch (Exception e) {

            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 별점 조회 api, 만약이 이미 별점이 있으면 수정
    @PutMapping("/rating")
    public ResponseEntity<?> ratingWebtoon(@RequestHeader("Authorization") String token,
                                           @RequestParam(name = "masterId") Long masterId,
                                           @RequestParam(name = "star") int star) {
        try {
            webtoonService.ratingWebtoon(token, masterId, star);
            return ResponseEntity.ok()
                    .body("sucess");
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 관심 웹툰 수정
    @PutMapping("/interest/{id}")
    public ResponseEntity<?> interestWebtoon(
            @RequestHeader("Authorization") String token
            , @PathVariable(name = "id") Long masterId) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.modifyInterest(token, masterId));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 상세 조회 api
    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReviewDetail(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable(name = "id") Long reviewId
    ){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getReview(reviewId,token));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 등록 api
    @PostMapping("/review/{id}")
    public ResponseEntity<?> PostWebtoonReview(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "id") Long masterId,
            @Validated @RequestBody PostWebtoonReviewRequest postWebtoonReviewRequest
    ){
        try {
            webtoonService.writeReview(token, masterId, postWebtoonReviewRequest.getContent());
            return ResponseEntity.ok()
                    .body("등록 완료");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰의 댓글 조회 api
    @GetMapping("/review/reply/{reviewId}")
    public ResponseEntity<?> getReviewReplyPage(Pageable pageable, @PathVariable("reviewId") Long reviewId){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getReviewReplyResponse(pageable,reviewId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 좋아요 수정
    @PatchMapping("/review/like/{id}")
    public ResponseEntity<?> modifyLike(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewId){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.modifyReviewLike(reviewId,token));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 댓글 등록
    @PostMapping("/review/reply/{id}")
    public ResponseEntity<?> addReviewReply(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewId,
            @Validated @RequestBody AddReviewReplyRequest addReviewReplyRequest)
            {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.addReviewReply(reviewId,token, addReviewReplyRequest.getContent()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 수정
    @PatchMapping("/review/{id}")
    public ResponseEntity<?> modifyReview(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewId,
            @Validated @RequestBody ModifyReviewRequest modifyReviewRequest){
        try {
            webtoonService.fixReview(reviewId,token,modifyReviewRequest.getContent());
            return ResponseEntity.ok()
                    .body("수정되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 삭제
    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> modifyReview(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewId){
        try {
            webtoonService.removeReview(reviewId,token);
            return ResponseEntity.ok()
                    .body("삭제되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 댓글 수정
    @PatchMapping("/review/reply/{id}")
    public ResponseEntity<?> modifyReviewReply(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewReplyId,
            @Validated @RequestBody ModifyReviewReplyRequest modifyReviewReplyRequest){
        try {
            webtoonService.fixReviewReply(reviewReplyId,token,modifyReviewReplyRequest.getContent());
            return ResponseEntity.ok()
                    .body("수정되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    // 리뷰 댓글 삭제
    @DeleteMapping("/review/reply/{id}")
    public ResponseEntity<?> removeReviewReply(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long reviewId){
        try {
            webtoonService.removeReviewReply(reviewId,token);
            return ResponseEntity.ok()
                    .body("삭제되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400)
                    .header(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
//    @GetMapping("/dbinit")
//    public String init(){
//        Map<String, Webtoon> mapJson = new HashMap<>();
//        Set<String> setNotNormal = new HashSet<>();
//        for (int idx=0; idx<=55000 ; idx+= 100) { // idx 상한선 나중에 바꾸기
//            if (idx % 1000 == 0) {
//                System.out.println("" + idx + "번 진행중");
//            }
//            webtoonService.webtoonInit(mapJson, setNotNormal, idx);
//        }
//        return "db init...";
//    }
}
