package com.java.JustThree.repository;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.dto.chat.ChatListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    public List<Chat> findByWebtoon_masterIdOrderByCreated(Long masterId);

    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
            "FROM Chat c " +
            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
            "ORDER BY c.created DESC")
    List<ChatListResponse> findAllLastChats();

    Chat findTopByWebtoon_MasterIdOrderByCreatedDesc(Long master_id);
    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
            "FROM Chat c " +
            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
            "ORDER BY c.webtoon.view DESC")
    // 채팅에서 웹툰 조회 많은 순 정렬
    List<ChatListResponse> findLastChatsOrderByHotWebtoon();

    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
            "FROM Chat c " +
            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
            "AND c.users.usersId = :user_id " +
            "ORDER BY c.webtoon.view DESC")
    List<ChatListResponse> findByUsers_UsersId(@Param("user_id")Long users_id);

//    @Query("SELECT c.we, MAX(c.created) From Chat c WHERE c.users.usersId = :users_id GROUP BY c.webtoon")
}
