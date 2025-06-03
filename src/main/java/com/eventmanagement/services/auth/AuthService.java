package com.eventmanagement.services.auth;

import com.eventmanagement.models.User;
import com.eventmanagement.services.auth.UserPrincipal;

public interface AuthService {
    UserPrincipal login(String username, String password);
    void logout(String token);
    boolean validateToken(String token);
    User getCurrentUser(String token);
}