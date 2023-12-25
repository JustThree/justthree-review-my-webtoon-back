package com.java.JustThree.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPWRequest {
    String password;
    String correctPassword;
}
