package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/adminpage")
    public String adminPage(){
        return "This is the admin page";
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/{id}/permissions")
    public String modifyUserPermissions(@PathVariable Long id, @RequestBody String newPermissions) {
        return userService.modifyPermission(newPermissions, id);
    }
    @GetMapping("/currentUser")
    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        HttpSession session = request.getSession(false);

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", authentication.getName());
        userData.put("roles", authentication.getAuthorities());
        return userData;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // Get existing session
        if (session != null) {
            session.invalidate(); // ✅ Destroy the session
        }
        SecurityContextHolder.clearContext(); // ✅ Clear authentication

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
//    private final AuthenticationManager authenticationManager;
//
//    public UserController(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            HttpSession session = httpRequest.getSession(true); // ✅ Creates a new session
//            session.setAttribute("user", authentication.getPrincipal()); // ✅ Stores user in session
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Login successful");
//            response.put("username", request.getUsername());
//            response.put("roles", authentication.getAuthorities());
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
//        }
//    }


}
