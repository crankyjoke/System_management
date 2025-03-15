package com.example.demo.controller;
import com.example.demo.Service.OrganizationTableService;
import com.example.demo.Service.UserPositionService;
import com.example.demo.model.User;
import com.example.demo.Service.UserService;
import com.example.demo.model.UserPosition;
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
import com.example.demo.model.Load;
@RestController
@RequestMapping("/api")

public class OrganizationController {
    private final OrganizationTableService organizationTableService;
    public OrganizationController(OrganizationTableService organizationTableService){
        this.organizationTableService = organizationTableService;
    }
    @PostMapping("/insertuser")
    public Load insertUser(@RequestBody Load load){
        Long id = load.getId();
        String username = load.getUsername();
        String organization = load.getOrganization();
        if(organizationTableService.insertIntoOrganization(organization,username,id)){
            return load;
        }
        else{
            return null;
        }

    }
    @GetMapping("/users/{organization}")
    public ResponseEntity<List<Object[]>> getAllUsersByOrganization(@PathVariable String organization) {
        List<Object[]> users = organizationTableService.fetchAllUsersByOrganization(organization);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

}
