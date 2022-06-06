package com.example.springsecurity.controller;

import com.example.springsecurity.dto.LoginRequestDto;
import com.example.springsecurity.dto.UserDto;
import com.example.springsecurity.response.ApiResponse;
import com.example.springsecurity.user.JwtAuthentication;
import com.example.springsecurity.user.JwtAuthenticationToken;
import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserLoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JwtRestController {
    private UserLoginService userLoginService;
    private AuthenticationManager authenticationManager;

    public JwtRestController(UserLoginService userLoginService, AuthenticationManager authenticationManager) {
        this.userLoginService = userLoginService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/user/login")
    @Transactional
    public ApiResponse<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        //1. 사용자 이름이 principal, 비밀번호가 credential으로 된 인증이 아직 되지 않은 인증객체를 생성한다.
        JwtAuthenticationToken beforeAuthentication = new JwtAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword()
        );

        //2. authenticationManger에 등록된 JwtAuthenticationProvider로 인증을 수행한다.
        Authentication afterAuthentication = authenticationManager.authenticate(beforeAuthentication);
        JwtAuthentication authenticated = (JwtAuthentication) afterAuthentication.getPrincipal();

        //3. 성공적으로 인증 객체가 반환된 경우 해당하는 유저가 가진 정보를 반환한다.
        User user = (User) afterAuthentication.getDetails();
        return new ApiResponse<>(new UserDto(
                authenticated.token, authenticated.username, user.getGroup().getName()
        ));
    }

    @GetMapping("/user/me")
    public ApiResponse<UserDto> me(@AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        //authentication 객체에서 principal과 credential을 확인하여 인증한 후
        //등록된 사용자인 경우 해당 유저의 정보를 반환한다.
        return new ApiResponse<UserDto>(userLoginService.findByLoginId(jwtAuthentication.username)
                .map(
                        (user) -> {
                            return new UserDto(jwtAuthentication.token, jwtAuthentication.username, user.getGroup().getName());
                        }
                )
                .orElseThrow(()-> new RuntimeException(jwtAuthentication.username + " - user not found")));
    }
}
