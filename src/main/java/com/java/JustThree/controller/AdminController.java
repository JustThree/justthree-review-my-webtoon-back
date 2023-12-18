package com.java.JustThree.controller;

import com.java.JustThree.dto.board.response.GetBoardListResponse;
import com.java.JustThree.service.AdminBoardService;
import com.java.JustThree.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminBoardService adminBoardService;

    //커뮤니티 게시글 목록(noticeYn=0) 조회
    @GetMapping("/boardlist")
    Page<GetBoardListResponse> getBoardList(@RequestParam(name = "page", defaultValue = "1") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "sortings", defaultValue = "sortDesc") String sortings,
                                            @RequestParam(name = "keyword", required = false) String keyword){
        String searchWord="";
        if(keyword == null){
            searchWord = "";
        }else{
            searchWord = keyword;
        }
        return adminBoardService.getBoardsByPage(page, size, sortings, searchWord);
    }

}
