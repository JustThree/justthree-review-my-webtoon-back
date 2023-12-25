package com.java.JustThree.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest

public class RefreshTokenRepotisoryTest {

    @Autowired
    private RefreshTokenRepository repository;

    @Order(1)
    @Test
    public void deletetoken(){
        String userEmail = "tjdtndlwkd@naver.com";
        repository.deleteByUser_UsersEmail(userEmail);

    }
}
