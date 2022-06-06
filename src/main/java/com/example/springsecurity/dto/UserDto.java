package com.example.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String token;
    private String username;
    private String group;

    @Override
    public String toString() {
        return "UserDto{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
