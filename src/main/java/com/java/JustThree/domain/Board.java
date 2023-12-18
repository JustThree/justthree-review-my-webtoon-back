package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private int viewCount;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime updated;

    @Column(name = "notice_yn", nullable = false)
    private int noticeYn;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "users_id")
    private Users users;

    public void updateBoard(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void plusViewCount(int viewCount){
        this.viewCount = viewCount;
    }

    @OneToMany(mappedBy = "board")
    private List<BoardReply> replies; // 댓글들을 저장하는 필드

    public int getReplyCount() {
        return replies.size(); // 댓글 수 반환
    }

}
