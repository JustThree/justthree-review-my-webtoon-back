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
    public Long addReply(AddBoardReplyReqeust addReplyReq){

        Optional<Board> optionalBoard = boardRepository.findById(addReplyReq.getBoardId());
        if(optionalBoard.isEmpty()){
            return null;
        }else{
            Board board = optionalBoard.get();
            BoardReply boardReply = BoardReply.builder()
                    .boardReplyContent(addReplyReq.getBoardReplyContent())
                    .board(board)
                    .users(addReplyReq.getUsers())
                    .parentReplyId(addReplyReq.getParentReplyId()) //대댓글 등록 시 0이면 안됨
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
                //대댓글 삭제
                List<BoardReply> boardReplyList = boardReplyRepository.findByParentReplyId(boardReplyId);
                boardReplyRepository.delete(boardReply);
                boardReplyRepository.deleteAll(boardReplyList);
                return "success";
            }
        }catch (Exception e){
            return e.getMessage();
        }

    }

    //댓글 조회
    public List<GetBoardReplyResponse> getBoardReplyList(long boardId){
        List<BoardReply> boardReplyList = boardReplyRepository.findByBoard_BoardIdIsOrderByCreatedDesc(boardId);
        log.info("댓글 조회");

        return boardReplyList.stream().map(GetBoardReplyResponse::entityToDTO).collect(Collectors.toList());
    }

}