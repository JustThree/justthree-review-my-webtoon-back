package com.java.JustThree.controller;

import com.java.JustThree.dto.JoinRequest;
import com.java.JustThree.dto.LoginRequest;
import com.java.JustThree.dto.RefreshTokenResponse;
import com.java.JustThree.dto.UsersResponse;
import com.java.JustThree.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
@Slf4j
public class UserController {

    private final UsersService usersService;


    @PostMapping(value = "/join")
    public Long join(@RequestBody JoinRequest joinDTO){

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

    @PostMapping("/email-verification")
    public ResponseEntity<Object> joinEmailVerification(
            @RequestParam("email") String email,
            @RequestParam("type") String type) {
        try {

            // 이메일 중복 검사 (존재하면 true)
            boolean isEmail = usersService.validateDuplicateEmail(email);

            // 이메일 전송 (인증번호 or 토큰)
            if (type.equals("join")) {
                if (isEmail) {
                    return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
                }
                usersService.sendJoinEmail(email);
            } else if (type.equals("reset-password")) {
                if (!isEmail) {
                    return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
                }
                usersService.sendResetPasswordEmail(email);
            } else {
                return ResponseEntity.badRequest().body("잘못된 요청입니다.");
            }
            return ResponseEntity.ok("성공적으로 이메일이 전송되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송에 실패하였습니다.");
        }
    }

    @RequestMapping("/verify-code")
    public ResponseEntity<Object> verifyJoinCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {
        try {
            String getCodeEmail = usersService.validateEmailVerification(code);
            if (!getCodeEmail.equals(email)) {
                return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
            }
            return ResponseEntity.ok("인증번호가 일치합니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문제가 발생하였습니다.");
        }
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Object> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean isNickname = usersService.validateDuplicateNickname(nickname);
        return isNickname ? ResponseEntity.badRequest().body("이미 존재하는 닉네임입니다.") : ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

}
