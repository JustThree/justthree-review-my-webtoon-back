package com.java.JustThree.repository;

import com.java.JustThree.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    public List<Review> findByUsers_UsersId(Long usersId);

}
