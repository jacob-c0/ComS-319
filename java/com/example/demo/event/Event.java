package com.example.demo.event;

import jakarta.persistence.*;
import com.example.demo.user.User;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

@Entity
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User owner;

    @NotEmpty(message = "The event title is required.")
    private String title;

    @NotEmpty(message = "The event description is required.")
    private String description;

    @NotEmpty(message = "The event visibilityType is required.")
    private String visibilityType;

    private String eventDate;

    private String eventTime;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<RecurDays> recurDays;
    
    @ManyToMany
    @JoinTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> attendees;
    
    // Getters and setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getOwner() {
        return this.owner;
    }
    
    public void setOwner(User owner) { this.owner = owner; }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVisibilityType() {
        return visibilityType;
    }
    
    public void setVisibilityType(String visibilityType) {
        this.visibilityType = visibilityType;
    }
    
    public String getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    
    public String getEventTime() {
        return eventTime;
    }
    
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    
    public Set<RecurDays> getRecurDays() {
        return recurDays;
    }
    
    public void setRecurDays(Set<RecurDays> recurDays) {
        this.recurDays = recurDays;
    }
    
    public List<User> getAttendees() {
        return attendees;
    }
    
    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }
}