package com.java.JustThree.service;

import com.java.JustThree.domain.Board;
import com.java.JustThree.dto.board.response.GetBoardListResponse;
import com.java.JustThree.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final BoardRepository boardRepository;

    //커뮤니티 글 목록 조회
    public Page<GetBoardListResponse> getBoardsByPage(int page, int size, String sortType, String keyword){
        // 정렬  기준(기본 최신순)
        Sort sortByDirection =Sort.by(Sort.Direction.DESC, "created");

        if(sortType.equals("sortDesc")){
            sortByDirection = Sort.by(Sort.Direction.DESC, "created");
        }else if(sortType.equals("sortAsc")) {
            sortByDirection = Sort.by(Sort.Direction.ASC, "created");
        }else if(sortType.equals("sortViewCntDesc")){
            sortByDirection = Sort.by(Sort.Direction.DESC, "viewCount")
                    .and(Sort.by(Sort.Direction.DESC, "created"));//조회수 →최신순
        }
        Pageable pageable = PageRequest.of(page-1, size, sortByDirection);

        System.out.println(keyword);

        // 검색어를 포함하는 게시글만 조회하는 쿼리 작성
        Specification<Board> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("noticeYn"), 0), // noticeYn이 0인 게시글만 조회
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"), // 제목에 검색어 포함
                                criteriaBuilder.like(root.get("content"), "%" + keyword + "%") // 내용에 검색어 포함
                        )
                );
        Page<Board> boardPage = boardRepository.findAll(specification, pageable);

        return boardPage.map(GetBoardListResponse::entityToDTO);

    }

    public Page<GetBoardListResponse> getNoticesByPage(String keyword, Pageable pageable) {
        Specification<Board> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("noticeYn"), 1), // noticeYn이 1인 공지 게시글만 조회
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"), // 제목에 검색어 포함
                                criteriaBuilder.like(root.get("content"), "%" + keyword + "%") // 내용에 검색어 포함
                        )
                );

        Page<Board> noticeBoardPage = boardRepository.findAll(specification, pageable);
        return noticeBoardPage.map(GetBoardListResponse::entityToDTO);
    }

}
