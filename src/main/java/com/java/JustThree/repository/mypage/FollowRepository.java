package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    public Long countByFollower_UsersId(Long usersId);//팔로워 카운트
    public Long countByFollowing_UsersId(Long usersId);//팔로잉 카운트

    public Long findAllByFollowing_UsersId(Long usersId);
    public Long findAllByFollower_UsersId(Long usersId);
}
