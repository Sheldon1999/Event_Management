package com.eventmanagement.repositories.impl;

import com.eventmanagement.config.DatabaseConfig;
import com.eventmanagement.exceptions.DataAccessException;
import com.eventmanagement.models.Participant;
import com.eventmanagement.repositories.ParticipantRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcParticipantRepository implements ParticipantRepository {
    private static final String FIND_BY_ID = "SELECT * FROM participants WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM participants";
    private static final String FIND_BY_EVENT_ID = "SELECT * FROM participants WHERE event_id = ?";
    private static final String FIND_BY_EMAIL = "SELECT * FROM participants WHERE email = ?";
    private static final String INSERT = """
        INSERT INTO participants (name, email, phone, department, event_id, registration_date)
        VALUES (?, ?, ?, ?, ?, ?)""";
    private static final String UPDATE = """
        UPDATE participants 
        SET name = ?, email = ?, phone = ?, department = ?, event_id = ?
        WHERE id = ?""";
    private static final String DELETE = "DELETE FROM participants WHERE id = ?";
    private static final String EXISTS_BY_ID = "SELECT COUNT(*) FROM participants WHERE id = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM participants";
    private static final String COUNT_BY_EVENT_ID = "SELECT COUNT(*) FROM participants WHERE event_id = ?";
    private static final String EXISTS_BY_EMAIL_AND_EVENT = """
        SELECT COUNT(*) FROM participants 
        WHERE LOWER(email) = LOWER(?) AND event_id = ?""";

    @Override
    public Optional<Participant> findById(Long id) {
        return queryParticipant(FIND_BY_ID, id);
    }

    @Override
    public List<Participant> findAll() {
        return queryParticipants(FIND_ALL);
    }

    @Override
    public List<Participant> findByEventId(Long eventId) {
        return queryParticipants(FIND_BY_EVENT_ID, eventId);
    }

    @Override
    public List<Participant> findByEmail(String email) {
        return queryParticipants(FIND_BY_EMAIL, email);
    }

    @Override
    public Participant save(Participant participant) {
        if (participant.getId() == null) {
            return insert(participant);
        } else {
            return update(participant);
        }
    }

    private Participant insert(Participant participant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            setParticipantStatementParameters(stmt, participant);
            stmt.setObject(6, participant.getRegistrationDate() != null ? 
                Timestamp.valueOf(participant.getRegistrationDate()) : 
                Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating participant failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    participant.setId(generatedKeys.getLong(1));
                } else {
                    throw new DataAccessException("Creating participant failed, no ID obtained.");
                }
            }
            return participant;
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting participant", e);
        }
    }

    private Participant update(Participant participant) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            
            setParticipantStatementParameters(stmt, participant);
            stmt.setLong(6, participant.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Updating participant failed, no rows affected.");
            }
            return participant;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating participant with id: " + participant.getId(), e);
        }
    }

    private void setParticipantStatementParameters(PreparedStatement stmt, Participant participant) 
            throws SQLException {
        stmt.setString(1, participant.getName());
        stmt.setString(2, participant.getEmail());
        stmt.setString(3, participant.getPhone());
        stmt.setString(4, participant.getDepartment());
        stmt.setLong(5, participant.getEventId());
    }

    @Override
    public void deleteById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting participant with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        return exists(EXISTS_BY_ID, id);
    }

    @Override
    public int count() {
        return count(COUNT);
    }

    @Override
    public int countByEventId(Long eventId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_EVENT_ID)) {
            stmt.setLong(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error counting participants for event id: " + eventId, e);
        }
    }

    @Override
    public boolean existsByEmailAndEventId(String email, Long eventId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_EMAIL_AND_EVENT)) {
            stmt.setString(1, email);
            stmt.setLong(2, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if participant exists with email: " + email, e);
        }
    }

    private Optional<Participant> queryParticipant(String sql, Object... params) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRowToParticipant(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error querying participant", e);
        }
    }

    private List<Participant> queryParticipants(String sql, Object... params) {
        List<Participant> participants = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(mapRowToParticipant(rs));
                }
            }
            return participants;
        } catch (SQLException e) {
            throw new DataAccessException("Error querying participants", e);
        }
    }

    private Participant mapRowToParticipant(ResultSet rs) throws SQLException {
        Participant participant = new Participant();
        participant.setId(rs.getLong("id"));
        participant.setName(rs.getString("name"));
        participant.setEmail(rs.getString("email"));
        participant.setPhone(rs.getString("phone"));
        participant.setDepartment(rs.getString("department"));
        participant.setEventId(rs.getLong("event_id"));
        participant.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        return participant;
    }

    private boolean exists(String sql, Object param) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if participant exists", e);
        }
    }

    private int count(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error counting participants", e);
        }
    }
}