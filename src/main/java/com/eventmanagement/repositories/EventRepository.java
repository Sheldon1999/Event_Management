package com.eventmanagement.repositories;

import com.eventmanagement.models.Event;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(Long id);
    List<Event> findAll();
    List<Event> findUpcoming();
    List<Event> findOngoing();
    List<Event> findPast();
    Event save(Event event);
    void deleteById(Long id);
    boolean existsById(Long id);
    int count();
    List<Event> findByNameContaining(String name);
    List<Event> findByLocation(String location);
}