package com.eventmanagement.models;

import java.time.LocalDateTime;

/**
 * Represents an event in the system with details like name, timing, and location.
 */
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Integer maxParticipants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Event() {}

    public Event(String name, String description, LocalDateTime startDateTime, 
                LocalDateTime endDateTime, String location, Integer maxParticipants) {
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startDateTime);
    }

    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDateTime) && now.isBefore(endDateTime);
    }

    public boolean isPast() {
        return LocalDateTime.now().isAfter(endDateTime);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", location='" + location + '\'' +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
