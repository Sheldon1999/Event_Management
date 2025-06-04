package com.eventmanagement.services.auth;

import com.eventmanagement.config.PasswordEncoder;
import com.eventmanagement.exceptions.AuthenticationException;
import com.eventmanagement.models.User;
import com.eventmanagement.repositories.UserRepository;
import com.eventmanagement.services.auth.UserPrincipal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, User> tokenUserMap = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenExpiryMap = new ConcurrentHashMap<>();
    private static final long TOKEN_VALIDITY_MS = 24 * 60 * 60 * 1000; // 24 hours

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserPrincipal login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            throw new AuthenticationException("Username and password are required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        String token = generateToken();
        tokenUserMap.put(token, user);
        tokenExpiryMap.put(token, System.currentTimeMillis() + TOKEN_VALIDITY_MS);
        
        return new UserPrincipal(user, token);
    }

    @Override
    public void logout(String token) {
        if (token != null) {
            tokenUserMap.remove(token);
            tokenExpiryMap.remove(token);
        }
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || !tokenUserMap.containsKey(token)) {
            return false;
        }
        
        Long expiryTime = tokenExpiryMap.get(token);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            tokenUserMap.remove(token);
            tokenExpiryMap.remove(token);
            return false;
        }
        
        // Refresh token expiry on validation
        tokenExpiryMap.put(token, System.currentTimeMillis() + TOKEN_VALIDITY_MS);
        return true;
    }

    @Override
    public User getCurrentUser(String token) {
        if (!validateToken(token)) {
            return null;
        }
        return tokenUserMap.get(token);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    // For testing and cleanup purposes
    void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        tokenExpiryMap.entrySet().removeIf(entry -> {
            if (entry.getValue() < currentTime) {
                tokenUserMap.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}