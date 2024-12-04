package com.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.entities.Event;
import com.project.request.EventCreateRequest;
import com.project.request.EventUpdateRequest;
import com.project.responses.EventResponse;
import com.project.services.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    public EventController(EventService eventService)
    {
        this.eventService = eventService;
    }

    @GetMapping("/suggest/{userId}")
    public List<EventResponse> getSuggestedEvents(@PathVariable Long userId)
    {
        return eventService.getSuggestedEvents(userId);
    }

    @PostMapping("/create/{userId}")
    public Event createEvent(@RequestBody EventCreateRequest eventCreateRequest, @PathVariable Long userId)
    {
        return eventService.createEvent(eventCreateRequest,userId);
    }

    @PostMapping("/join/{eventId}/{userId}")
    public ResponseEntity<String> joinEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        try {
            System.out.println("kontrol");
            eventService.joinEvent(eventId, userId);
            return ResponseEntity.ok("Kullanıcı etkinliğe başarıyla katıldı!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventById(@PathVariable Long eventId)
    {
        return eventService.getEventById(eventId);
    }

    @GetMapping("/joined/{userId}")
    public List<EventResponse> getJoinedEventsByUserId(@PathVariable Long userId)
    {
        return eventService.getJoinedEventsByUserId(userId);
    }

    @GetMapping("/created/{userId}")
    public List<EventResponse> getCreatedEventsByUserId(@PathVariable Long userId)
    {
        return eventService.getCreatedEventsByUserId(userId);
    }
    @PutMapping("/{eventId}")
    public Event updateEventById(@PathVariable Long eventId, @RequestBody EventUpdateRequest eventUpdateRequest)
    {
        return eventService.updateEventById(eventId, eventUpdateRequest);
    }


    @DeleteMapping("/{eventId}")
    public void deleteEventById(@PathVariable Long eventId)
    {
        eventService.deleteEventById(eventId);
    }

}
