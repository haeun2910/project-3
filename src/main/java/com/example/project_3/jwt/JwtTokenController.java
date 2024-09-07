package com.example.project_3.jwt;

import com.example.project_3.jwt.dto.JwtRequestDto;
import com.example.project_3.jwt.dto.JwtResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/token")
public class JwtTokenController {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    public JwtTokenController(
            JwtTokenUtils tokenUtils,
            UserDetailsService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/issue")
    public JwtResponseDto issueJwt(
            @RequestBody
            JwtRequestDto dto
    ) {
        // 아이디로 사용자 조회
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(dto.getUsername());
        } catch (UsernameNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(
                dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // JWT 발급
        String jwt = tokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }
}
