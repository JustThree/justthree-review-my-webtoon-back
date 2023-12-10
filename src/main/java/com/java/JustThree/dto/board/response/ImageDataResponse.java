package com.java.JustThree.dto.board.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDataResponse {
    private Long imgId;
    private String accessUrl;
    private String originName;
}
