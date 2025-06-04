package com.eventmanagement.repositories;

import com.eventmanagement.models.Participant;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {
    Optional<Participant> findById(Long id);
    List<Participant> findAll();
    List<Participant> findByEventId(Long eventId);
    List<Participant> findByEmail(String email);
    Participant save(Participant participant);
    void deleteById(Long id);
    boolean existsById(Long id);
    int count();
    int countByEventId(Long eventId);
    boolean existsByEmailAndEventId(String email, Long eventId);
}