package com.java.JustThree.repository.mypage;
import com.java.JustThree.domain.Follow;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.admin.FollowCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    public Long countByFollower_UsersId(Long usersId);//팔로워 카운트
    public Long countByFollowing_UsersId(Long usersId);//팔로잉 카운트

    public List<Follow> findAllByFollower_UsersId(Long usersId);//내가 팔로잉 하는 사람들
    public List<Follow> findAllByFollowing_UsersId(Long usersId);//날 팔로우 하는 사람들.
    Optional<Follow> findByFollowerAndFollowing(Users follower, Users following);//

    boolean existsByFollowerAndFollowing(Users follower, Users following);
    boolean existsByFollower_UsersIdAndFollowing_UsersId(Long follower, Long following);
    // 탑 n개 팔로우 조회를 위한 쿼리
    @Query("select NEW com.java.JustThree.dto.admin.FollowCount(u.usersNickname, COUNT(f)) " +
            "from Users u " +
            "left join Follow f " +
            "on f.follower.usersId = u.usersId " +
            "group by u.usersId " +
            "order by count(f) desc ")
    Page<FollowCount> topNFollowerUser(Pageable pageable);
    // 탑 n개 팔로윙 조회를 위한 쿼리
    @Query("select NEW com.java.JustThree.dto.admin.FollowCount(u.usersNickname, COUNT(f)) " +
            "from Users u " +
            "left join Follow f " +
            "on f.following.usersId = u.usersId " +
            "group by u.usersId " +
            "order by count(f) desc ")
    Page<FollowCount> topNFollowingUser(Pageable pageable);


}
