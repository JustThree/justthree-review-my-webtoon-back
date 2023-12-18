package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @ToString(exclude = "board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_reply_id")
    private Long boardReplyId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="board_id", referencedColumnName = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)   //Board 엔티티 데이터 삭제 시 같이 삭제
    private Board board;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(name = "board_reply_content")
    private String boardReplyContent;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime updated;

    @Column(name = "parent_reply_id")
    private long parentReplyId; //부모 댓글 아이디

    public void updateBoardReply(String boardReplyContent){
        this.boardReplyContent = boardReplyContent;
    }

}
