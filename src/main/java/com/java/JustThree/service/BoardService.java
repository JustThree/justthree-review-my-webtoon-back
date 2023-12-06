package com.java.JustThree.service;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.dto.board.AddBoardRequest;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
        /*log.info("ADD BOARD");
        log.info(""+addBoardRequest.getImageFiles()[0]);
        log.info(""+addBoardRequest.getImageFiles()[0].getOriginalFilename());*/

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

}
