package com.example.demo.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String hello() {
        return "Hello! Any authenticated user (ADMIN or USER) can see this.";
    }
}
