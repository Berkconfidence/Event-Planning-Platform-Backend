package com.project.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    private String eventName;

    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private Integer duration; // Dakika cinsinden süre (örn. 90 dakika)

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String category;

    @ManyToMany
    @JoinTable(
            name = "participants", // Ortak tablo adı
            joinColumns = @JoinColumn(name = "event_id"), // Event tablosunun foreign key'i
            inverseJoinColumns = @JoinColumn(name = "user_id") // User tablosunun foreign key'i
    )
    private Set<User> participants = new HashSet<>();

    public Long getId() {
        return eventID;
    }

    public void setId(long eventID) {
        this.eventID = eventID;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
