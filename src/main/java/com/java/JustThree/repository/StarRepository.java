package com.java.JustThree.repository;

import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import com.java.JustThree.domain.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,Long> {
    public List<Star> findByUsers(Users users);


}
