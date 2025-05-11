package com.example.userservice.config.security;

import com.example.userservice.dto.RequestLogin;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Environment env;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug(((User) authResult.getPrincipal()).getUsername());
        String email = ((User) authResult.getPrincipal()).getUsername();
        UserEntity userEntity = userRepository.findByEmail(email);
        UserDTO userDetails = UserDTO.change(userEntity);
        log.debug("userDetails {}", userDetails);

        Key key = Keys.hmacShaKeyFor(env.getProperty("token.secret").getBytes());

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(new Date(
                        System.currentTimeMillis()
                                + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(key)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }


}
