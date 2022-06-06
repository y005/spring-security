package com.example.springsecurity.filter;

import com.example.springsecurity.config.Jwt;
import com.example.springsecurity.user.JwtAuthentication;
import com.example.springsecurity.user.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final String header;

    private final Jwt jwt;

    public JwtAuthenticationFilter(String header, Jwt jwt) {
        this.header = header;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(req);
            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    String username = claims.username;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if ((username.length() > 0) && (authorities.size() > 0)) {
                        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                                new JwtAuthentication(token, username), null, authorities
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                catch (Exception e) {
                    //do error handler
                    System.out.println("error");
                }
            }
        }
        else {
            SecurityContextHolder.getContext().getAuthentication();
        }
        chain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (token != null) {
            try {
                return URLEncoder.encode(token, "UTF-8");
            }
            catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0 ?
                emptyList() :
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }
}
