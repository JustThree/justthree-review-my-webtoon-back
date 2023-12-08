package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,Long> {


    public List<Star> findByUsers_UsersId(Long usersId);

    Long countByUsers_UsersId(Long usersId);
}
