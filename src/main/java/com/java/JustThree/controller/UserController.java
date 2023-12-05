package com.java.JustThree.controller;

import com.java.JustThree.dto.JoinDTO;
import com.java.JustThree.dto.UsersDTO;
import com.java.JustThree.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UsersService usersService;

    @PostMapping(value = "/join")
    public UsersDTO join(@RequestBody JoinDTO joinDTO){

        return usersService.insertUsers(joinDTO);
    }


}
