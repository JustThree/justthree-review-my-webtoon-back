package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_like_id")
    private Long boardLikeId;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
