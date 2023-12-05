package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;    // PK, AutoIncrement

    @ManyToOne(fetch = FetchType.LAZY)
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime created;

}
