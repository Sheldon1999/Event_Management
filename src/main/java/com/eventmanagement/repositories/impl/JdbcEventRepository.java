package com.eventmanagement.repositories.impl;

import com.eventmanagement.config.DatabaseConfig;
import com.eventmanagement.exceptions.DataAccessException;
import com.eventmanagement.models.Event;
import com.eventmanagement.repositories.EventRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEventRepository implements EventRepository {
    private static final String FIND_BY_ID = "SELECT * FROM events WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM events ORDER BY start_datetime";
    private static final String FIND_UPCOMING = "SELECT * FROM events WHERE start_datetime > ? ORDER BY start_datetime";
    private static final String FIND_ONGOING = "SELECT * FROM events WHERE start_datetime <= ? AND end_datetime >= ?";
    private static final String FIND_PAST = "SELECT * FROM events WHERE end_datetime < ? ORDER BY start_datetime DESC";
    private static final String FIND_BY_NAME = "SELECT * FROM events WHERE LOWER(name) LIKE ? ORDER BY start_datetime";
    private static final String FIND_BY_LOCATION = "SELECT * FROM events WHERE LOWER(location) LIKE ? ORDER BY start_datetime";
    private static final String INSERT = """
        INSERT INTO events (name, description, start_datetime, end_datetime, location, max_participants, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)""";
    private static final String UPDATE = """
        UPDATE events 
        SET name = ?, description = ?, start_datetime = ?, end_datetime = ?, 
            location = ?, max_participants = ?, updated_at = CURRENT_TIMESTAMP 
        WHERE id = ?""";
    private static final String DELETE = "DELETE FROM events WHERE id = ?";
    private static final String EXISTS_BY_ID = "SELECT COUNT(*) FROM events WHERE id = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM events";

    @Override
    public Optional<Event> findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRowToEvent(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding event by id: " + id, e);
        }
    }

    @Override
    public List<Event> findAll() {
        return queryEvents(FIND_ALL);
    }

    @Override
    public List<Event> findUpcoming() {
        return queryEvents(FIND_UPCOMING, LocalDateTime.now());
    }

    @Override
    public List<Event> findOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return queryEvents(FIND_ONGOING, now, now);
    }

    @Override
    public List<Event> findPast() {
        return queryEvents(FIND_PAST, LocalDateTime.now());
    }

    @Override
    public Event save(Event event) {
        if (event.getId() == null) {
            return insert(event);
        } else {
            return update(event);
        }
    }

    private Event insert(Event event) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            setEventStatementParameters(stmt, event);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating event failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getLong(1));
                } else {
                    throw new DataAccessException("Creating event failed, no ID obtained.");
                }
            }
            return event;
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting event", e);
        }
    }

    private Event update(Event event) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            
            setEventStatementParameters(stmt, event);
            stmt.setLong(7, event.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Updating event failed, no rows affected.");
            }
            return event;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating event with id: " + event.getId(), e);
        }
    }

    private void setEventStatementParameters(PreparedStatement stmt, Event event) throws SQLException {
        stmt.setString(1, event.getName());
        stmt.setString(2, event.getDescription());
        stmt.setTimestamp(3, Timestamp.valueOf(event.getStartDateTime()));
        stmt.setTimestamp(4, Timestamp.valueOf(event.getEndDateTime()));
        stmt.setString(5, event.getLocation());
        if (event.getMaxParticipants() != null) {
            stmt.setInt(6, event.getMaxParticipants());
        } else {
            stmt.setNull(6, Types.INTEGER);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting event with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if event exists with id: " + id, e);
        }
    }

    @Override
    public int count() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error counting events", e);
        }
    }

    @Override
    public List<Event> findByNameContaining(String name) {
        return queryEvents(FIND_BY_NAME, "%" + name.toLowerCase() + "%");
    }

    @Override
    public List<Event> findByLocation(String location) {
        return queryEvents(FIND_BY_LOCATION, "%" + location.toLowerCase() + "%");
    }

    private List<Event> queryEvents(String sql, Object... params) {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRowToEvent(rs));
                }
            }
            return events;
        } catch (SQLException e) {
            throw new DataAccessException("Error querying events", e);
        }
    }

    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setStartDateTime(rs.getTimestamp("start_datetime").toLocalDateTime());
        event.setEndDateTime(rs.getTimestamp("end_datetime").toLocalDateTime());
        event.setLocation(rs.getString("location"));
        event.setMaxParticipants(rs.getInt("max_participants"));
        if (rs.wasNull()) {
            event.setMaxParticipants(null);
        }
        event.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        event.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return event;
    }
}