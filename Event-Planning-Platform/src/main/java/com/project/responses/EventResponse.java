package com.project.responses;

import com.project.entities.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventResponse {

    //istediğin bilgileri al (göstermek istediğin?)
    private Long eventID;
    private String eventName;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private Integer duration;
    private String location;
    private String category;
    private boolean isRecommended;

    public EventResponse(Event entity){
        this.eventID = entity.getId();
        this.eventName = entity.getEventName();
        this.description = entity.getDescription();
        this.date = entity.getDate();
        this.time = entity.getTime();
        this.duration = entity.getDuration();
        this.location = entity.getLocation();
        this.category = entity.getCategory();
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
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

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }
}
