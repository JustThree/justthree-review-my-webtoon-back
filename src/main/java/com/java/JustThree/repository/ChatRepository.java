package com.java.JustThree.repository;

import com.java.JustThree.domain.Chat;
import com.java.JustThree.dto.chat.ChatListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    public boolean existsByWebtoon_masterId(Long masterId);

    public List<Chat> findByWebtoon_masterIdOrderByCreated(Long masterId);

    @Query("SELECT NEW com.java.JustThree.dto.chat.ChatListResponse(" +
            "c.contents, " +
            "c.webtoon.masterId, " +
            "c.webtoon.title, " +
            "c.created, " +
            "c.users.usersNickname, " +
            "c.webtoon.imageUrl) " +
            "FROM Chat c " +
            "WHERE c.created = (SELECT MAX(c2.created) FROM Chat c2 " +
            "WHERE c2.webtoon = c.webtoon AND c2.created = (SELECT MAX(c3.created) FROM Chat c3 WHERE c3.webtoon = c.webtoon)) " +
            "ORDER BY c.created DESC")
    List<ChatListResponse> findLatestChats();


//    public List<Chat> findLastChatAll();
//    public List<Chat> findByWebtoon_Title(String title);
//    public Chat findTop1ByWebtoon_MastrId(Long masterId);
//
//    public List<Chat> findByOrderByCreated();
//
//
//    public List<Chat> findByUsers_UsersId(Long user_id);

}
