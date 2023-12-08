package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon,Long> {
}
