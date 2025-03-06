package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private Long accesspage;
    private String permission;

    public User() {
    }
    public void setAccesspage(){
        this.accesspage = 1L;
    }
    public void setAccesspage(Long accesspage) {
        this.accesspage = accesspage;
    }

    public Long getAccesspage() {
        return this.accesspage;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPermission(String permission) {
        System.out.println("permission updated");
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
