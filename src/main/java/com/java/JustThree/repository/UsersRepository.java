package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByUsersEmail(String usersEmail);
}
