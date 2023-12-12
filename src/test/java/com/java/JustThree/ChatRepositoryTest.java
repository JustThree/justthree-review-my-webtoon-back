package com.java.JustThree;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.dto.chat.ChatListResponse;
import com.java.JustThree.repository.ChatRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    private ChatRepository repository;

    @BeforeEach()
    void pr() {
        System.out.println("==========================================================");
    }

    @Test
    @Order(1)
    void test1() {
        List<ChatListResponse> resp = repository.findLatestChats();
        resp.forEach(System.out::println);

    }
}
