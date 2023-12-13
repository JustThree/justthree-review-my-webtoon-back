package com.java.JustThree.repository.mypage;

import com.java.JustThree.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest,Long> {
    public List<Interest> findByUsers_UsersId(Long usersId);
    public Long countByUsers_UsersId(Long usersId);
    public Optional<Interest> findByUsers_UsersIdIsAndWebtoon_MasterIdIs(Long users_usersId, Long webtoon_masterId);

}
