package com.java.JustThree.controller;

import com.java.JustThree.service.UsersService;
import com.java.JustThree.service.WebtoonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/webtoon")
@RestController
@AllArgsConstructor
public class MainPageController {
    WebtoonService webtoonService;


    // 단건 조회
    // 성인웹툰 거르기...
    @GetMapping("/{id}")
    public ResponseEntity<?> webtoonDetail(
//            @RequestHeader("Authorization") String token,
                                           @PathVariable(name = "id") Long id){

        String token = null;
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonDetail(token,id));
        } catch (IllegalArgumentException e){
            return ResponseEntity
                    .notFound()
                    .header("error",e.getMessage())
                    .build();
        } catch (NullPointerException e){
            return ResponseEntity
                    .status(404)
                    .header("error",e.getMessage())
                    .build();
        }
    }
    @GetMapping("/webtoonlist")
    public ResponseEntity<?> webtoonKeywordList(@PageableDefault(size = 25) Pageable pageable,@RequestParam(name = "keyword") String keyword){
        try {
            return ResponseEntity.ok()
                            .body(webtoonService.getWebtoonKeyword(pageable,keyword));
        } catch (Exception e){
            return ResponseEntity.status(404)
                    .header("error",e.getMessage())
                    .build();
        }
    }
    @GetMapping("")
    public ResponseEntity<?> webtoonList(@PageableDefault(size = 24)Pageable pageable, @RequestParam(name = "genre",required = false)String genre, @RequestParam(name = "order",required = false)String order) {
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonPage(pageable,genre,order));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }

    }
    @GetMapping("/search")
    public ResponseEntity<?> searchList(@PageableDefault(size = 24)Pageable pageable, @RequestParam(name = "type")String type, @RequestParam(name = "word")String word){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.searchWebtoon(pageable,type,word));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
    @PutMapping("/rating")
    public ResponseEntity<?> ratingWebtoon(@RequestHeader("Authorization") String token,
                                           @RequestParam(name = "masterId")Long masterId,
                                           @RequestParam(name = "star")int star){
        try {
            webtoonService.ratingWebtoon(token,masterId,star);
            return ResponseEntity.ok()
                    .body("sucess");
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .header("error", e.getMessage())
                    .build();
        }
    }
        @GetMapping("/reviews/{id}")
    public ResponseEntity<?> reviewsWebtoon(@PathVariable(name = "id") Long id, @PageableDefault(size = 12)  Pageable pageable){
        try {
            return ResponseEntity.ok()
                    .body(webtoonService.getWebtoonReviewsPage(id, pageable));
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
