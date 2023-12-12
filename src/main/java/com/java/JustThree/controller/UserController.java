package com.java.JustThree.controller;

import com.java.JustThree.dto.JoinRequest;
import com.java.JustThree.dto.LoginRequest;
import com.java.JustThree.dto.RefreshTokenResponse;
import com.java.JustThree.dto.UsersResponse;
import com.java.JustThree.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
@Slf4j
public class UserController {

    private final UsersService usersService;


    @PostMapping(value = "/join")
    public Long join(JoinRequest joinDTO){

        return usersService.insertUsers(joinDTO);
    }

    @GetMapping(value = "/tui")
    public UsersResponse asdf(@RequestHeader("Authorization") String token){

        log.info("Authorization이 도착했습니다");
        log.info(token);

        log.info("userId가 도착했습니다");
        log.info(usersService.something(token));

        return usersService.getUserInfo(token);
    }

    @PostMapping(value = "/logout")
    public void logout(@RequestBody LoginRequest loginRequest) {
        log.info(loginRequest.getUsersEmail());

        usersService.logout(loginRequest.getUsersEmail());
    }

    @GetMapping(value = "/auth/accessoken")
    public String recreateAccessToken(@RequestHeader("Authorization") String token){

        return usersService.getNewAccessToken(token);
    }

}
