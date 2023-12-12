package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    public Long countByFollower_UsersId(Long usersId);//팔로워 카운트
    public Long countByFollowing_UsersId(Long usersId);//팔로잉 카운트

    public List<Follow> findAllByFollowing_UsersId(Long usersId);//내가 팔로우 하는 사람들
    public List<Follow> findAllByFollower_UsersId(Long usersId);//날 팔로우 하는 사람들.

}
