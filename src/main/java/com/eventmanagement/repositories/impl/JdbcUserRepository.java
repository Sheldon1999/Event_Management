package com.eventmanagement.repositories.impl;

import com.eventmanagement.config.DatabaseConfig;
import com.eventmanagement.exceptions.DataAccessException;
import com.eventmanagement.models.User;
import com.eventmanagement.repositories.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_ALL = "SELECT * FROM users";
    private static final String INSERT = """
        INSERT INTO users (username, password_hash, email, role, created_at, updated_at) 
        VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)""";
    private static final String UPDATE = """
        UPDATE users SET username = ?, password_hash = ?, email = ?, role = ?, updated_at = CURRENT_TIMESTAMP 
        WHERE id = ?""";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String EXISTS_BY_USERNAME = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String EXISTS_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM users";

    @Override
    public Optional<User> findById(Long id) {
        return queryUser(FIND_BY_ID, id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return queryUser(FIND_BY_USERNAME, username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return queryUser(FIND_BY_EMAIL, email);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all users", e);
        }
        return users;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    private User insert(User user) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole().name());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new DataAccessException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user", e);
        }
    }

    private User update(User user) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole().name());
            stmt.setLong(5, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Updating user failed, no rows affected.");
            }
            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user with id: " + user.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user with id: " + id, e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return exists(EXISTS_BY_USERNAME, username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return exists(EXISTS_BY_EMAIL, email);
    }

    @Override
    public int count() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error counting users", e);
        }
    }

    private Optional<User> queryUser(String sql, Object... params) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRowToUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error querying user", e);
        }
    }

    private boolean exists(String sql, Object param) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if user exists", e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}