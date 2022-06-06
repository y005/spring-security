package com.example.springsecurity.provider;

import com.example.springsecurity.config.Jwt;
import com.example.springsecurity.user.JwtAuthentication;
import com.example.springsecurity.user.JwtAuthenticationToken;
import com.example.springsecurity.user.User;
import com.example.springsecurity.user.UserLoginService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final Jwt jwt;
    private final UserLoginService userLoginService;

    public JwtAuthenticationProvider(Jwt jwt, UserLoginService userLoginService) {
        this.jwt = jwt;
        this.userLoginService = userLoginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(jwtAuthenticationToken.getPrincipal()),
                    jwtAuthenticationToken.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        try {
            User user = userLoginService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getGroup().getAuthorities();
            String token = getToken(user.getLoginId(), authorities);
            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(new JwtAuthentication(token, user.getLoginId()), null, authorities);
            authenticated.setDetails(user);
            return authenticated;
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private String getToken(String loginId, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        return jwt.sign(Jwt.Claims.from(loginId, roles));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
