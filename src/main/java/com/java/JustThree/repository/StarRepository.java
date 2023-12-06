package com.java.JustThree.repository;

import com.java.JustThree.domain.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,Long> {


    //User의 userID로 조회
    public List<Star> findByUsers_UsersId(Integer usersId);

}
