package com.eventmanagement.repositories;

import com.eventmanagement.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    int count();
}