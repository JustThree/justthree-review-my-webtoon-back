package com.java.JustThree.repository;

import com.java.JustThree.domain.ChatRoom;
import com.java.JustThree.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    public List<ChatRoom> findTop5ByUsers(Users users);
}
