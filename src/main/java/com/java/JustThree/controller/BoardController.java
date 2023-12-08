package com.java.JustThree.controller;

import com.java.JustThree.dto.board.AddBoardRequest;
import com.java.JustThree.dto.board.UpdateBoardRequest;
import com.java.JustThree.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    //커뮤니티 글 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveBoard(@ModelAttribute AddBoardRequest addBoardRequest){
        System.out.println(addBoardRequest);
        try{
            Long res = boardService.addBoard(addBoardRequest);
            return ResponseEntity.ok("글 등록 "+res);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 커뮤니티 글 수정
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBoard(@PathVariable("id") long boardId, @ModelAttribute UpdateBoardRequest updateBoardRequest){
        updateBoardRequest.setBoardId(boardId);
        log.info("수정요청  >>"+updateBoardRequest);
        try{
            Long res = boardService.updateBoard(updateBoardRequest);
            return ResponseEntity.ok().body("글 수정 "+res);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }
    
    //커뮤니티 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeBoard(@PathVariable("id")long id){
        log.info("id >>"+id);
        try{
            String res = boardService.removeBoard(id);
            return ResponseEntity.ok(id+"글 삭제 "+res);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
