package com.example.demo.controller;
import com.example.demo.Service.UserPositionService;
import com.example.demo.model.User;
import com.example.demo.Service.UserService;
import com.example.demo.repository.UserPositionRepository;
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
import com.example.demo.model.UserPosition;
@RestController
@RequestMapping("/api/userPosition")
public class UserPositionController {
    private final UserPositionService userPositionService;
    private final AuthenticationManager authenticationManager;
    public UserPositionController(UserPositionService userPositionService, AuthenticationManager authenticationManager){
        this.userPositionService = userPositionService;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/{id}")
    public Optional<UserPosition> getUserPositionById(@PathVariable Long id){
        return userPositionService.getUserPositionById(id);
    }
    @GetMapping("/getall")
    public List<UserPosition> getAll(){
        return userPositionService.getAll();
    }
    @PutMapping("/createUserPosition")
    public UserPosition createUserPosition(@RequestBody UserPosition userPosition){
        return userPositionService.createUserPosition(userPosition);
    }
    @PutMapping("/updateUserPosition")
    public UserPosition updateUserPosition(@RequestBody UserPosition userPosition){
        return userPositionService.updateUserPosition(userPosition);
    }
}

