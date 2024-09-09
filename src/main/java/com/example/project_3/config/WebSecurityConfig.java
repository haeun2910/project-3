package com.example.project_3.config;

import com.example.project_3.jwt.JwtTokenFilter;
import com.example.project_3.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;
    public WebSecurityConfig(
            JwtTokenUtils tokenUtils,
            UserDetailsService userService
    ) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/error", "/token/issue", "/views/**","/static/**", "/test/**")
                            .permitAll();
                    auth.requestMatchers("/users/my-profile",
                                    "/users/update",
                                    "/users/image",
                                    "/users/delete",
                                    "/users/approve-business",
                                    "/users/business-application",
                                    "/users/reject-business",
                                    "/users/apply-business"
                                    )
                            .authenticated();
                    auth.requestMatchers("/users/create").anonymous();
                    auth.requestMatchers("/default-role").hasRole("DEFAULT");
                    auth.requestMatchers("/admin-role").hasRole("ADMIN");
                    auth.requestMatchers("/user-role","/users/apply-business").hasRole("USER");
                    auth.requestMatchers("/business-role").hasRole("BUSINESS");
                })
                .addFilterBefore(
                        new JwtTokenFilter(
                                tokenUtils,
                                userService
                        ),
                        AuthorizationFilter.class
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/users/my-profile")
                        .failureUrl("/users/login?fail")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/login")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
