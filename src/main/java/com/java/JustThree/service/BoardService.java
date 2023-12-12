package com.java.JustThree.service;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.dto.board.request.AddBoardRequest;
import com.java.JustThree.dto.board.response.GetBoardListResponse;
import com.java.JustThree.dto.board.response.GetBoardOneResponse;
import com.java.JustThree.dto.board.request.UpdateBoardRequest;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //이미지 파일 등록
    private final BoardImageRepository boardImageRepository;
    private final BoardImageService boardImageService;

    //커뮤니티 글 등록
    @Transactional
    public Long addBoard(AddBoardRequest addBoardRequest){
        Board newBoard = Board.builder()
                .title(addBoardRequest.getTitle())
                .content(addBoardRequest.getContent())
                .noticeYn(addBoardRequest.getNoticeYn())
                .users(addBoardRequest.getUsers())
                .build();
        boardRepository.save(newBoard);
        //첨부파일 있을 경우
        if(addBoardRequest.getImageFiles() != null && !addBoardRequest.getImageFiles()[0].isEmpty()){
            for(MultipartFile imgFile: addBoardRequest.getImageFiles()){
                String storedName = boardImageService.uploadFile(imgFile);
                String accessUrl = boardImageService.getAccessUrl(storedName);

                BoardImage boardImage = BoardImage.builder()
                        .board(newBoard)
                        .accessUrl(accessUrl)
                        .originName(imgFile.getOriginalFilename())
                        .storedName(storedName)
                        .build();
                boardImageRepository.save(boardImage);
            }
        }
        return newBoard.getBoardId();
    }
    //커뮤니티 글 상세 조회(댓글, 좋아요 구현 후 보완 필요)
    public GetBoardOneResponse getBoardOne(long boardId){
        //Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if(optionalBoard.isEmpty()){
            return null;
        }else {
            Board board = optionalBoard.get();
            List<BoardImage> boardImageList = boardImageRepository.findByBoard(board);
            //log.info("board1  >>"+board);
            //log.info("boardImgList  >>"+boardImageList);
            //조회수 증가
            board.plusViewCount(board.getViewCount() + 1);
            boardRepository.save(board);
            //log.info("board2  >>"+board);
            return GetBoardOneResponse.entityToDTO(board, boardImageList);
        }
    }


    //커뮤니티 글 수정 (추후 수정 필요)
    @Transactional
    public Long updateBoard(UpdateBoardRequest updateBoardReq){
        //Optional<Board> oldOptionalBoard = boardRepository.findById(updateBoardReq.getBoardId());
        Board oldBoard = boardRepository.findById(updateBoardReq.getBoardId())
                .orElseThrow(NoSuchElementException::new);
        oldBoard.updateBoard(updateBoardReq.getTitle(), updateBoardReq.getContent());
        boardRepository.save(oldBoard);

        List<BoardImage> oldBoardImageList = boardImageRepository.findByBoard(oldBoard);

        if(oldBoardImageList.isEmpty()){//기존 첨부파일 없을 경우
            //수정요청에 첨부파일 있을 경우
            if(updateBoardReq.getImageFiles() != null && !updateBoardReq.getImageFiles()[0].isEmpty()) {
                for(MultipartFile imgFile: updateBoardReq.getImageFiles()){
                    String storedName = boardImageService.uploadFile(imgFile);
                    String accessUrl = boardImageService.getAccessUrl(storedName);

                    BoardImage boardImage = BoardImage.builder()
                            .board(oldBoard)
                            .accessUrl(accessUrl)
                            .originName(imgFile.getOriginalFilename())
                            .storedName(storedName)
                            .build();

                    boardImageRepository.save(boardImage);
                }
            }
        }else{ //기존 첨부파일 있을 경우
            log.info("기존 이미지파일  >>" + oldBoardImageList);
            //수정요청에 첨부파일 있을 경우
            if(updateBoardReq.getImageFiles() != null && !updateBoardReq.getImageFiles()[0].isEmpty()) {
                //DB & S3에서 기존 파일 삭제
                List<BoardImage> oldBImgList = boardImageRepository.findByBoard(oldBoard);
                for(BoardImage oldBoardImg: oldBImgList){
                    String storedName = oldBoardImg.getStoredName();
                    boardImageService.deleteFile(storedName); //AWS S3에서 삭제
                    boardImageRepository.delete(oldBoardImg); //DB Table(BoardImage)에서 삭제
                }
                // 수정요청 첨부파일 저장
                for(MultipartFile imgFile: updateBoardReq.getImageFiles()) {
                    String storedName = boardImageService.uploadFile(imgFile);
                    String accessUrl = boardImageService.getAccessUrl(storedName);

                    BoardImage boardImage = BoardImage.builder()
                            .board(oldBoard)
                            .accessUrl(accessUrl)
                            .originName(imgFile.getOriginalFilename())
                            .storedName(storedName)
                            .build();
                    boardImageRepository.save(boardImage);
                }
            }
        }
        return updateBoardReq.getBoardId();
    }

    //커뮤니티 글 삭제
    @Transactional
    public String removeBoard(long boardId){
        try{
            Optional<Board> boardOptional = boardRepository.findById(boardId);
            if(boardOptional.isEmpty()){
                return "NotFoundBoard";
            }else {
                Board board = boardOptional.get();
                //S3에서 삭제
                List<BoardImage> boardImageList = boardImageRepository.findByBoard(board);
                //boardImageList.stream().forEach(System.out::println);
                log.info("boardImageList 크기  >>"+boardImageList.size());
                for(BoardImage boardImage: boardImageList){
                    String storedName = boardImage.getStoredName();
                    boardImageService.deleteFile(storedName);
                }

                boardRepository.delete(board);
                return "success";
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }
    //커뮤니티 글 목록 조회
    public List<GetBoardListResponse> getBoardsByPage(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);

        // noticeYn이 0인 게시글만 조회하는 쿼리 작성
        Specification<Board> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("noticeYn"), 0);


        Page<Board> boardPage = boardRepository.findAll(specification, pageable);
        List<Board> boardList = boardPage.getContent();

        /*
        return boardList.stream()
                .map(board -> GetBoardListResponse.entityToDTO(board))
                .collect(Collectors.toList());
         */

        return boardList.stream()
                .map(GetBoardListResponse::entityToDTO)
                .collect(Collectors.toList());
    }

}