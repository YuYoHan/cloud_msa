package com.example.userservice.config.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserService userService;
    private final Environment env;
    private final AuthenticationConfiguration authenticationConfiguration;

    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);

    @Bean
    protected SecurityFilterChain configurer(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable());

        http
                .authorizeHttpRequests(r ->
                        r.requestMatchers("/users/**").permitAll());

        http
                .addFilter(getAuthenticationFilter());

//        http
//                .addFilterBefore();

        // 이거가 없으면 /h2-console 접근 안됨
        http
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }

    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
