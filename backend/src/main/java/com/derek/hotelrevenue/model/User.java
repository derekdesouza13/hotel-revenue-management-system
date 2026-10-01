package com.derek.hotelrevenue.model;

import com.derek.hotelrevenue.enums.UserRole;

public class User {

    private Long id;

    private String username;

    private String email;

    private UserRole role;

    private boolean active;

    public User() {
        this.active = true;
    }

    public User(
            Long id,
            String username,
            String email,
            UserRole role
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.active = true;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}