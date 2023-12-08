
package com.java.JustThree.repository;

import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.board.BoardRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private BoardImageRepository boardImageRepo;

    @Autowired
    private UsersRepository userRepo;


    @BeforeEach
    void hr(){
        System.out.println("=".repeat(80));
    }
    @Test
    @Order(1)
    void 게시물이미지조회(){
        //long id = 7L;
        //Board board = boardRepo.findById(id).get();
        //System.out.println(board);
        //List<BoardImage> list = boardImageRepo.findByBoard(board);
        //list.stream().forEach(System.out::println);
    }
/*
    @Test
    @Order(1)
    void 커뮤니티_글_상세조회(){
        //given
        Users users = Users.builder()
                .usersId(1)
                .usersNickname("하유라")
                .usersPw("1234")
                .usersEmail("yura@gmail.com")
                .created(LocalDateTime.now())
                .build();
        userRepo.save(users);
        Board board = Board.builder()
                .boardId(1L)
                .users(users)
                .title("웹툰 플랫폼 추천 TOP3")
                .content("네이버/카카오/카카오페이지")
                .created(LocalDateTime.now())
                .viewCount(0)
                .noticeYn(0)
                .build();
        boardRepo.save(board);
        //when
        Optional<Users> users1 = userRepo.findById(1);
        Optional<Board> bookone2 = boardRepo.findById(1L);
        //Board boardone = boardRepo.findById(1L).get();
        //then
        if(users1.isEmpty()){
            System.out.println("사용자 없음");
        }else {
            System.out.println(users1.get());
        }
        if(bookone2.isEmpty()){
            System.out.println("없음");
        }else{
            System.out.println(bookone2.get());
        }
    }
    */
}
