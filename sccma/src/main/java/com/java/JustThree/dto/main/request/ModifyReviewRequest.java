package com.java.JustThree.dto.main.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyReviewRequest {
    @NotEmpty
    @Size(min = 5, max= 200, message = "내용은 5자에서 200자 사이여야 됩니다.")
    private String content;
}
