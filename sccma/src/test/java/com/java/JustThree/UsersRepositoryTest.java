package com.java.JustThree;


import com.java.JustThree.domain.Users;
import com.java.JustThree.repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository ur;

    @BeforeEach()
    void pr() {
        System.out.println("==========================================================");
    }

    @Test
    @Order(1)
    void one1(){
        List<Users> list = ur.findAll();
        list.stream().forEach(System.out::println);
    }

}
