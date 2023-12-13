package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,Long> {


    public List<Star> findByUsers_UsersId(Long usersId);

    public List<Star> findByUsers_UsersId_OrderByStarValDesc(Long usersId);
    public List<Star> findByUsers_UsersId_OrderByStarVal(Long usersId);

    public List<Star> findByUsers_UsersIdAndStarVal(Long usersId,int starVal);

    Long countByUsers_UsersId(Long usersId);
}
