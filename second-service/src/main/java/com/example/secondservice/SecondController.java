package com.example.secondservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
public class SecondController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Second project";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request")String header) {
        return "Hello World in Second message";
    }

    @GetMapping("/check")
    public String check() {
        return "Hi, there. This is a message from Second Service";
    }
}
