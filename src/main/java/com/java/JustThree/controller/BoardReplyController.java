package com.java.JustThree.controller;

import com.java.JustThree.dto.board.request.AddBoardReplyReqeust;
import com.java.JustThree.dto.board.request.AddBoardRequest;
import com.java.JustThree.dto.board.request.UpdateBoardReplyReqeust;
import com.java.JustThree.dto.board.request.UpdateBoardRequest;
import com.java.JustThree.dto.board.response.GetBoardListResponse;
import com.java.JustThree.dto.board.response.GetBoardOneResponse;
import com.java.JustThree.exception.BoardNotFoundException;
import com.java.JustThree.service.board.BoardReplyService;
import com.java.JustThree.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boardreply")
public class BoardReplyController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;

    //커뮤니티 글 댓글 등록
    @PostMapping
    public ResponseEntity<?> addBoardReply(@RequestBody AddBoardReplyReqeust addBoardReplyReq,
                                           @RequestHeader(value = "Authorization", required = false) String token){
        System.out.println(addBoardReplyReq);
        log.info("token  >>" + token);
        try{
            Long res = boardReplyService.addReply(addBoardReplyReq, token);
            log.info("댓글 등록 pk"+res);
            return ResponseEntity.ok("1");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //커뮤니티 글 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> addBoardReply(@PathVariable("id") long replyId,
                                           @RequestBody UpdateBoardReplyReqeust updateBoardReplyReq){
        System.out.println(replyId);
        updateBoardReplyReq.setBoardReplyId(replyId);
        System.out.println(updateBoardReplyReq);
        try{
            Long res = boardReplyService.updateBoardReply(updateBoardReplyReq);
            log.info("댓글 수정 pk"+res);
            return ResponseEntity.ok("1");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //커뮤니티 글 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeBoardReply(@PathVariable("id")long id){
        log.info("id >>"+id);
        try{
            String res = boardReplyService.removeBoardReply(id);
            return ResponseEntity.ok(id+"댓글 삭제 "+res);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
