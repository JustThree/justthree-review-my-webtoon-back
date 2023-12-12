package com.java.JustThree.controller;

import com.java.JustThree.dto.board.request.AddBoardRequest;
import com.java.JustThree.dto.board.request.AddBoardReplyReqeust;
import com.java.JustThree.dto.board.response.GetBoardListResponse;
import com.java.JustThree.dto.board.response.GetBoardOneResponse;
import com.java.JustThree.dto.board.request.UpdateBoardRequest;
import com.java.JustThree.exception.BoardNotFoundException;
import com.java.JustThree.service.BoardReplyService;
import com.java.JustThree.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;

    //커뮤니티 글 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveBoard(@ModelAttribute AddBoardRequest addBoardRequest){
        System.out.println(addBoardRequest);
        try{
            Long res = boardService.addBoard(addBoardRequest);
            log.info("글 등록 pk"+res);
            return ResponseEntity.ok("1");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 커뮤니티 글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardOne(@PathVariable("id") long id){
        log.info("찾아야할 id"+id);
        try{
            GetBoardOneResponse boardOneRes = boardService.getBoardOne(id);
            log.info(""+boardOneRes);
            if( boardOneRes != null){
                return ResponseEntity.status(HttpStatus.OK).body(boardOneRes);
            }else{
                throw new BoardNotFoundException(id+"Not Found");
            }
        }catch (BoardNotFoundException bnfe){
            log.error(bnfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bnfe.getMessage());
        }
    }

    // 커뮤니티 글 수정
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBoard(@PathVariable("id") long boardId,
                                              @ModelAttribute UpdateBoardRequest updateBoardRequest){
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
    //커뮤니티 게시글 목록(noticeYn=0) 조회
    @GetMapping
    List<GetBoardListResponse> getBoardList(@RequestParam(name = "page", defaultValue = "1") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "sortings", defaultValue = "sortDesc") String sortings,
                                            @RequestParam(name = "keyword", required = false) String keyword){

        return boardService.getBoardsByPage(page, size, sortings, keyword);
    }
    //커뮤니티 게시글 목록 키워드 검색 조회
    /*@GetMapping("/search")
    List<GetBoardListResponse> getBoardsByKeyword(@RequestParam(name = "page", defaultValue = "1") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "keyword", required = false) String keyword){
        return boardService.searchBoardsByKeyword(keyword, page, size);
    }*/

    //커뮤니티 글 댓글 등록
    @PostMapping("/reply")
    public ResponseEntity<?> addBoardReply(@RequestBody AddBoardReplyReqeust addBoardReplyReq){
        System.out.println(addBoardReplyReq);
        try{
            Long res = boardReplyService.addReply(addBoardReplyReq);
            log.info("댓글 등록 pk"+res);
            return ResponseEntity.ok("1");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
