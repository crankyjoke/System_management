package com.example.demo.controller;

import com.example.demo.Service.OrganizationTableService;
import com.example.demo.model.Load;  // example model if you want
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    private final OrganizationTableService organizationTableService;

    public OrganizationController(OrganizationTableService organizationTableService) {
        this.organizationTableService = organizationTableService;
    }


    @PostMapping("/insertuser")
    public ResponseEntity<?> insertUser(@RequestBody Load load) {
        if (load.getOrganization() == null || load.getOrganization().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Organization name cannot be empty");
        }
        if (organizationTableService.insertIntoOrganization(load.getOrganization(), load.getUsername(), load.getId())) {
            return ResponseEntity.ok(load);
        }
        return ResponseEntity.status(500).body("Failed to insert user");
    }


    @GetMapping("/users/{organization}")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers(@PathVariable String organization) {

        List<Object[]> rows = organizationTableService.fetchAllUsersByOrganization(organization);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("name", row[1]);
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add/table")
    public ResponseEntity<String> addTable(@RequestBody String organization) {
        if (organization == null || organization.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Organization name cannot be empty");
        }

        try {
            organizationTableService.createOrganizationTable(organization);
            return ResponseEntity.ok("Table created for organization: " + organization);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating table: " + e.getMessage());
        }
    }

    @GetMapping("/organization/tables")
    public ResponseEntity<List<String>> getAllOrganizationTables() {
        List<String> tables = organizationTableService.getAllOrganizationTables();
        if (tables.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tables);
    }
}
