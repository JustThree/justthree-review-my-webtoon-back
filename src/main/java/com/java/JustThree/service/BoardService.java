package com.java.JustThree.service;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.dto.board.AddBoardRequest;
import com.java.JustThree.dto.board.UpdateBoardRequest;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


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
    //커뮤니티 글 수정
    @Transactional
    public Long updateBoard(UpdateBoardRequest updateBoardReq){
        //Optional<Board> oldOptionalBoard = boardRepository.findById(updateBoardReq.getBoardId());
        Board oldBoard = boardRepository.findById(updateBoardReq.getBoardId())
                .orElseThrow(NoSuchElementException::new);
        oldBoard.updateBoard(updateBoardReq.getTitle(), updateBoardReq.getContent());
        boardRepository.save(oldBoard);

        List<BoardImage> oldBoardImageList = boardImageRepository.findByBoard(oldBoard);

        if(oldBoardImageList.isEmpty()){//기존 첨부파일 없을 경우
            System.out.println("비어있음");
            //첨부파일 있을 경우
            if(updateBoardReq.getImageFiles() != null && !updateBoardReq.getImageFiles()[0].isEmpty()) {
                //String res = boardImageService.updateFile(updateBoardReq.getBoardId(), updateBoardReq.getImageFiles());
                //System.out.println("결과  >>" + res);
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
        }else{ //기존 첨부파일 없을 경우
            log.info("기존 이미지파일  >>" + oldBoardImageList);

        }

/*
      //첨부파일 있을 경우
        if(updateBoardReq.getImageFiles() != null && !updateBoardReq.getImageFiles()[0].isEmpty()){
            String res = boardImageService.updateFile(updateBoardReq.getBoardId(), updateBoardReq.getImageFiles());
            System.out.println("결과  >>"+res);

 */
            /*List<BoardImage> boardImageList = boardImageRepository.findByBoard_BoardIdIs(updateBoardReq.getBoardId());
            //List<Long> boardimgIdList = boardImageRepository.findImgIdByBoardId(updateBoardReq.getBoardId());
            for(BoardImage boardImg: boardImageList){
                BoardImage oldBoardImage = boardImageRepository.findById(boardImg.getImgId()).get();
                for(MultipartFile imgFile: updateBoardReq.getImageFiles()){

                    String storedName = boardImageService.updateFile(boardImg.getImgId(), imgFile);
                    String accessUrl = boardImageService.getAccessUrl(storedName);

                    oldBoardImage.updateFile(accessUrl, imgFile.getOriginalFilename(), storedName);
                    boardImageRepository.save(oldBoardImage);

                }
            }*/
/*
        }
*/
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

}
