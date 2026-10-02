package com.derek.hotelrevenue.dto;

import com.derek.hotelrevenue.enums.UserRole;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private boolean active;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String username,
            String email,
            UserRole role,
            boolean active
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
}