package com.java.JustThree.service.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardReply;
import com.java.JustThree.dto.board.request.AddBoardReplyReqeust;
import com.java.JustThree.repository.board.BoardReplyRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}