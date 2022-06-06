package com.example.springsecurity.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="group_id")
    private Group group;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "passwd")
    private String passwd;

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, passwd))
            throw new IllegalArgumentException("Bad credential");
    }
}