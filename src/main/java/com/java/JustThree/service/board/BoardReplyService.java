package com.java.JustThree.service.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardReply;
import com.java.JustThree.dto.board.request.AddBoardReplyReqeust;
import com.java.JustThree.dto.board.request.UpdateBoardReplyReqeust;
import com.java.JustThree.dto.board.response.GetBoardReplyResponse;
import com.java.JustThree.repository.board.BoardReplyRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardReplyService {

    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;

    //댓글 등록
    @Transactional
    public Long addReply(AddBoardReplyReqeust addReplyReqeust){

        Optional<Board> optionalBoard = boardRepository.findById(addReplyReqeust.getBoardId());
        if(optionalBoard.isEmpty()){
            return null;
        }else{
            Board board = optionalBoard.get();
            BoardReply boardReply = BoardReply.builder()
                    .boardReplyContent(addReplyReqeust.getBoardReplyContent())
                    .board(board)
                    .users(addReplyReqeust.getUsers())
                    .build();
            boardReplyRepository.save(boardReply);
            return boardReply.getBoardReplyId();
        }
    }

    //댓글 수정
    @Transactional
    public Long updateBoardReply(UpdateBoardReplyReqeust updateBoardReplyReq){
        Optional<BoardReply> optionalBoardReply = boardReplyRepository.findById(updateBoardReplyReq.getBoardReplyId());
        if (optionalBoardReply.isEmpty()){
           return null;
        }else{
            BoardReply oldBoardReply = optionalBoardReply.get();
            oldBoardReply.updateBoardReply(updateBoardReplyReq.getBoardReplyContent());
            boardReplyRepository.save(oldBoardReply);
            return updateBoardReplyReq.getBoardReplyId();
        }
    }

    //댓글 삭제
    @Transactional
    public String removeBoardReply(long boardReplyId){
        try{
            Optional<BoardReply> optionalBoardReply = boardReplyRepository.findById(boardReplyId);
            if(optionalBoardReply.isEmpty()){
                return "NotFoundBoardReply";
            }else{
                BoardReply boardReply = optionalBoardReply.get();
                boardReplyRepository.delete(boardReply);
                return "success";
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    //댓글 조회
    public List<GetBoardReplyResponse> getBoardReplyList(long boardId){
        List<BoardReply> boardReplyList = boardReplyRepository.findByBoard_BoardIdIs(boardId);
        log.info("댓글 조회");

        return boardReplyList.stream().map(GetBoardReplyResponse::entityToDTO).collect(Collectors.toList());
       /* 
       //댓글 페이징
       Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<BoardReply> boardReplyPage = boardReplyRepository.findAll(pageable);
        List<BoardReply> boardReplyList = boardReplyPage.getContent();
        if(boardReplyList.isEmpty()){
            System.out.println(boardReplyList);
        }
        return boardReplyList.stream().map(GetBoardReplyResponse::entityToDTO).collect(Collectors.toList())
        ;*/
    }

}