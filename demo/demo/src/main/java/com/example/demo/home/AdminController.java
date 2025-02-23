package com.example.demo.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/secret")
    public String secret() {
        return "This is the secret admin endpoint, accessible ONLY by ADMIN role.";
    }
}
