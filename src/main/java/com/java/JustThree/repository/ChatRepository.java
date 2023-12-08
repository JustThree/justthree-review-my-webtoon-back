package com.java.JustThree.repository;

import com.java.JustThree.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    public boolean existsByWebtoon_mastrId(Long masterId);
    
    public List<Chat> findByWebtoon_mastrIdOrderByCreated(Long masterId);

}
