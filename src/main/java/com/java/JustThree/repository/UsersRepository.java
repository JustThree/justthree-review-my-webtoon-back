package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByUsersEmail(String usersEmail);

    public Optional<Users> findByUsersNickname(String usersNickname);

    Page<Users> findAll(Pageable pageable);

    Page<Users> findByUsersNicknameContaining(String usersNickname, Pageable pageable);
    Page<Users> findByUsersEmailContaining(String usersEmail, Pageable pageable);

}
