package com.java.JustThree.service.board;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardLike;
import com.java.JustThree.dto.board.request.AddBoardLikeRequest;
import com.java.JustThree.repository.board.BoardLikeRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    //게시글 좋아요 등록
    @Transactional
    public Long addLike(AddBoardLikeRequest addBoardLikeReq){
        Optional<Board> optionalBoard = boardRepository.findById(addBoardLikeReq.getBoardId());
        if(optionalBoard.isEmpty()){
            return null;
        }else {
            Board board = optionalBoard.get();
            BoardLike newBoardLike = BoardLike.builder()
                    .users(addBoardLikeReq.getUsers())
                    .board(board)
                    .build();
            boardLikeRepository.save(newBoardLike);
            return newBoardLike.getBoardLikeId();
        }
    }
    //게시글 좋아요 취소
    @Transactional
    public String removeBoardLike(long boardLikeId){
        try{
            Optional<BoardLike> optionalBoardLike = boardLikeRepository.findById(boardLikeId);
            if(optionalBoardLike.isEmpty()){
                return "NotFound";
            }else{
                BoardLike boardLike = optionalBoardLike.get();
                boardLikeRepository.delete(boardLike);
                return "success";
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }
    //게시글 좋아요 조회(좋아요 여부)
    public boolean getBoardLike(Long boardId, Long usersId){
        return boardLikeRepository.existsBoardLikeByBoard_BoardIdAndUsers_UsersId(boardId, usersId);
    }

    // 하나의 게시글에 좋아요 개수
    public long getBoardLikeCount(Long boardId){
        return boardLikeRepository.countByBoard_BoardId(boardId);
    }

}
