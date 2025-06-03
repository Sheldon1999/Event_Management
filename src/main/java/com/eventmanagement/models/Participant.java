package com.eventmanagement.models;

import java.time.LocalDateTime;

/**
 * Represents a participant registered for an event in the system.
 */
public class Participant {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Long eventId;
    private LocalDateTime registrationDate;
    // Reference to the event (optional, for easier access)
    private Event event;

    // Constructors
    public Participant() {}

    public Participant(String name, String email, String phone, String department, Long eventId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.eventId = eventId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            this.eventId = event.getId();
        }
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", eventId=" + eventId +
                ", registrationDate=" + registrationDate +
                '}';
    }
}