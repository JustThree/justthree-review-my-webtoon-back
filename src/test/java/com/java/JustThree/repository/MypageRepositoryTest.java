package com.java.JustThree.repository;

import com.java.JustThree.domain.Star;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
public class MypageRepositoryTest {
    @Autowired
    com.java.JustThree.repository.StarRepository StarRepository;
    @Autowired
    UsersRepository UsersRepository;
    @BeforeEach()
    void pr() {
        System.out.println("=".repeat(80));
    }

    @Test
    @Order(1)
    public void findAll() {
        List<Star> list = StarRepository.findAll();
        list.forEach(System.out::println);
    }
    //평가한 웹툰
    @Test
    @Order(2)
    public void selectList(){
        List<Star> star =StarRepository.findByUsers_UsersId(1L);
        star.forEach(System.out::println);
    }

}
