package com.java.JustThree.dto.board;

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
