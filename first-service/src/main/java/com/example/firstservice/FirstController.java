package com.example.firstservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service")
@RequiredArgsConstructor
@Slf4j
public class FirstController {
    // 몇번 port로 동작하는지 확인
    private final Environment environment;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to First project";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request")String header) {
        return "Hello World in First message";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.debug("Server Port={}", request.getServerPort());
        return String.format("Hi, there. This is a message from First Service on PORT %s",
                environment.getProperty("local.server.port"));
    }
}
