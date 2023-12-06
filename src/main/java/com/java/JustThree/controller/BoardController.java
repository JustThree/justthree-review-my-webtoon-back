package com.java.JustThree.controller;

import com.java.JustThree.dto.board.AddBoardRequest;
import com.java.JustThree.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveBoard(@ModelAttribute AddBoardRequest addBoardRequest){
        System.out.println(addBoardRequest);
        try{
            Long res = boardService.addBoard(addBoardRequest);
            return ResponseEntity.ok("글 등록"+res);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
