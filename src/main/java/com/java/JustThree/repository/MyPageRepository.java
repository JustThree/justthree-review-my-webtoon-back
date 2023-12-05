package com.java.JustThree.repository;

import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageRepository extends JpaRepository<Users,Integer> {

}
