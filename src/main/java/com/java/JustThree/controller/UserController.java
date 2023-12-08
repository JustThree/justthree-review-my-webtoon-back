package com.java.JustThree.controller;

import com.java.JustThree.dto.JoinRequest;
import com.java.JustThree.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UsersService usersService;

    @PostMapping(value = "/join")
    public Long join(JoinRequest joinDTO){

        return usersService.insertUsers(joinDTO);
    }

    @GetMapping(value = "/authorizetest")
    public String asdf(){

        return "테스트용";
    }


}
