package com.example.demo.model;

public class Load {
    private Long id;
    private String username;
    private String organization;

    public Load() {
    }

    public Load(Long id, String username, String organization) {
        this.id = id;
        this.username = username;
        this.organization = organization;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}