package com.example.springsecurity.user;

import com.example.springsecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserLoginService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserLoginService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        User user = userRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLoginId(String loginId) {
        return  userRepository.findByLoginId(loginId);
    }
}
