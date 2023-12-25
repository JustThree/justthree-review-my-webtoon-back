package com.java.JustThree.dto.board.request;

import com.java.JustThree.domain.Users;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardLikeRequest {
    private Long boardId;
    private Users users;
}
