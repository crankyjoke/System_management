package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/adminpage")
    public String adminPage(){
        return "This is the admin page";
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getall")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userPayload) {
//        System.out.println(11111111);

        User updatedUser = userService.updateUser(id, userPayload);


        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedUser);
        }

    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable Long id){
        boolean deleted = userService.deleteUser(id);
        return deleted;
    }
    public class UsernameRequest {
        private String username;
        public String getUsername(){
            return this.username;
        }
    }

    @PostMapping("/modify/username/{id}")
    public boolean modifyUsername(@PathVariable Long id, @RequestBody UsernameRequest request) {
        // Now "request.getUsername()" is just "1111"
        return userService.updateUsername(request.getUsername(), id);
    }


    @PostMapping("/modify/email/{id}")
    public boolean modifyEmail(@PathVariable Long id, @RequestBody String newEmail){
        return userService.updateEmail(newEmail, id);
    }



    @PutMapping("/setnewPermission/{id}")
    public String modifyUserPermissions(@PathVariable Long id, @RequestBody String newPermissions) {
        return userService.modifyPermission(newPermissions, id);
    }
    @GetMapping("/currentUser")
    public Map<String, Object> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        return Map.of(
                "username", authentication.getName(),
                "roles",    authentication.getAuthorities()
        );
    }

//    @GetMapping("/currentUser")
//    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
//        }
//        HttpSession session = request.getSession(false);
//
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("username", authentication.getName());
//        userData.put("roles", authentication.getAuthorities());
//        return userData;
//    }

//    @PostMapping("/logout")
////    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
////        HttpSession session = request.getSession(false);
////        if (session != null) {
////            session.invalidate();
////        }
////        SecurityContextHolder.clearContext();
////        System.out.println("logged out");
////        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
////    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // This does *everything*: session.invalidate, clear context,
            // delete remember‑me cookie (if configured), publish LogoutSuccessEvent…
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }



//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User request, HttpServletRequest httpRequest) {
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
