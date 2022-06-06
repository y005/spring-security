package com.example.springsecurity.config;

import com.example.springsecurity.filter.JwtAuthenticationFilter;
import com.example.springsecurity.provider.JwtAuthenticationProvider;
import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    @Primary
    public AuthenticationManagerBuilder myAuthenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor, UserService userService, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        //authenticationManagerBuilder.userDetailsService(userService);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
        return authenticationManagerBuilder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        Jwt jwt = applicationContext.getBean(Jwt.class);
        JwtConfigure jwtConfigure = applicationContext.getBean(JwtConfigure.class);
        return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/api/user/me").hasAnyRole("USER","ADMIN")
                    .anyRequest().permitAll()
                    .and()
//                .formLogin()
//                    .defaultSuccessUrl("/")
//                    .permitAll()
//                    .and()
//                .logout()
//                    .logoutSuccessUrl("/")
//                    .and()
//                .rememberMe()
//                    .rememberMeParameter("remember-me")
//                    .tokenValiditySeconds(300)
//                    .and()
                .formLogin()
                    .disable()
                .csrf()
                    .disable()
                .headers()
                    .disable()
                .httpBasic()
                    .disable()
                .rememberMe()
                    .disable()
                .logout()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())
                    .and()
//                .securityContext()
//                    .securityContextRepository(securityContextRepository())
//                    .and()
                .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/assets/**", "/h2-console/**");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("text/plan");
                response.getWriter().write("ACCESS DENIED");
                response.getWriter().close();
            }
        };
    }
}
