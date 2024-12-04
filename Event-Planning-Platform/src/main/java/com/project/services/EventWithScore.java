package com.project.services;

import com.project.entities.Event;
import com.project.responses.EventResponse;

class EventWithScore {
    private Event event;
    private int score;

    public EventWithScore(Event event, int score) {
        this.event = event;
        this.score = score;
    }

    public EventWithScore(Event event) {
        this.event = event;
        this.score = 0;
    }

    public void incrementScore(int amount) {
        this.score += amount;
    }

    public EventResponse toEventResponse() {
        return new EventResponse(event);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
