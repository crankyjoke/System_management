package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserPosition {
    @Id
    private Long id;
    @ElementCollection
    private List<String> positionName;
    private Long balance;
    private String organization;

    public UserPosition(){
        
    }
    public UserPosition(Long id){
        this.id = id;
        this.positionName = List.of(new String[]{"Default"});
        this.balance = 0L;
        this.organization = "Default";
    }
    public UserPosition(Long id, List<String> positionName, Long balance, String organization) {
        this.id = id;
        this.positionName = positionName;
        this.balance = balance;
        this.organization = organization;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setPositionName(List<String> positionName) {
        this.positionName = positionName;
    }

    public List<String> getPositionName() {
        return this.positionName;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return this.balance;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return this.organization;
    }


}
