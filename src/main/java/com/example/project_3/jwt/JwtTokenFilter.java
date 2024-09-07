package com.example.project_3.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService userService;
    public JwtTokenFilter(
            JwtTokenUtils tokenUtils,
            UserDetailsService userService
    ) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. 요청에서 Authorization 헤더를 추출한다.
        String authHeader =
//                request.getHeader("Authorization");
                request.getHeader(HttpHeaders.AUTHORIZATION);
        // 만약 헤더가 없을 경우, 인증되지 않은 사용자이다.
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 헤더의 형태를 확인한다. (Bearer jwt)
        String[] headerSplit = authHeader.split(" ");
        // 헤더의 형태가 기대한 형태가 아니라면 인증되지 않은 사용자이다.
        if (!(headerSplit.length == 2 && headerSplit[0].equals("Bearer"))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. JWT가 정상인지 확인한다.
        String jwt = headerSplit[1];
        // JWT가 정상이 아니다.
        if (!tokenUtils.validate(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 여기까지 검증이 끝나면 JWT는 정상, 내용을 확인할 수 있다.
        // 4. Spring Security에서 인증이 된 사용자라는 것을 알 수 있도록
        // 인증 정보를 생성해서 등록해준다.
        // 4-1. 인증 정보를 담는 SecurityContext를 만든다.
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 4-2. 사용자 정보를 JWT에서 추출한다.
        String username = tokenUtils
                .parseClaims(jwt)
                .getSubject();
        // 4-2-2. 사용자 정보를 service에서 회수한다.
        UserDetails userDetails = userService.loadUserByUsername(username);
        // 4-3. Authentication을 만든다.
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
        // 4-4. Authentication을 SecurityContext에 담아준다.
        context.setAuthentication(authentication);
        // 4-5. SecurityContextHolder에게 SecurityContext를 넘겨준다.
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
