package com.java.JustThree.repository;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.dto.chat.ChatListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c.webtoon.masterId from Chat c GROUP BY c.webtoon.masterId order by c.webtoon.view")
    List<Long> findAllChat();

    @Query("SELECT c.webtoon.masterId from Chat c GROUP BY c.webtoon.masterId order by c.webtoon.view desc ")
    List<Long> findAllChatOrderByView();

    Chat findTopByWebtoon_MasterIdOrderByCreatedDesc(Long masterId);

    List<Chat> findByWebtoon_masterIdOrderByCreated(Long masterId);

    // Query로 전체를 한 번에 찾는 건 시간이 오래 걸림
//    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
//            "FROM Chat c " +
//            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
//            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
//            "ORDER BY c.created DESC")
//    List<ChatListResponse> findAllLastChats();

    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
            "FROM Chat c " +
            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
            "ORDER BY c.webtoon.view DESC")
    // 채팅에서 웹툰 조회 많은 순 정렬
    List<ChatListResponse> findLastChatsOrderByHotWebtoon();

//    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(c) " +
//            "FROM Chat c " +
//            "WHERE c.users.usersId = :users_id " +
//            "group by c.webtoon.masterId " +
//            "ORDER BY c.created DESC")
//    List<ChatListResponse> findByUsers_UsersId(@Param("users_id")Long users_id);

    List<Chat> findByUsers_UsersId(@Param("users_id")Long users_id);

    boolean existsByUsers_UsersIdAndWebtoon_MasterId(Long usersId, Long masterId);
}
