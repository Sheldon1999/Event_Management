package com.eventmanagement.services.auth;

import com.eventmanagement.models.User;

public class UserPrincipal {
    private final User user;
    private final String token;

    public UserPrincipal(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public boolean isAdmin() {
        return user != null && user.getRole() == User.Role.ADMIN;
    }
}