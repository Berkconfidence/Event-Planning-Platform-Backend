package com.project.services;

import com.project.entities.Point;
import com.project.entities.User;
import com.project.repository.PointRepository;
import com.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.project.entities.Event;
import com.project.repository.EventRepository;
import com.project.request.EventCreateRequest;
import com.project.request.EventUpdateRequest;
import com.project.responses.EventResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private UserService userService;
    private PointRepository pointRepository;
    public EventService(EventRepository eventRepository, UserRepository userRepository, UserService userService, PointRepository pointRepository)
    {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.pointRepository = pointRepository;
    }

    public List<EventResponse> getSuggestedEvents(Long userId) {
        User user = userService.getUserById(userId);
        if(user == null) {
            return eventRepository.findAll().stream()
                    .map(EventResponse::new)
                    .collect(Collectors.toList());
        }

        // 1. Kullanıcının ilgi alanları
        Set<String> userInterests = user.getInterests() != null ?
                Arrays.stream(user.getInterests().toLowerCase().split(" "))
                        .collect(Collectors.toSet()) :
                new HashSet<>();

        // 2. Kullanıcının geçmiş etkinlik kategorileri ve frekansları
        Map<String, Integer> categoryFrequency = getPastEventCategoryFrequency(userId);

        // 3. Kullanıcının henüz katılmadığı etkinlikleri al
        List<Event> availableEvents = eventRepository.findEventsNotJoinedByUser(userId);

        return availableEvents.stream()
                .map(event -> {
                    EventWithScore eventWithScore = new EventWithScore(event);
                    String category = event.getCategory().toLowerCase();

                    // İlgi alanlarına göre puanlama
                    if (userInterests.contains(category)) {
                        eventWithScore.incrementScore(10);
                    }

                    // Geçmiş katılımlara göre puanlama
                    Integer pastParticipation = categoryFrequency.get(category);
                    if (pastParticipation != null) {
                        eventWithScore.incrementScore(Math.min(pastParticipation * 3, 15));
                    }

                    // Açıklama içeriğinde ilgi alanı kontrolü
                    String description = event.getDescription().toLowerCase();
                    for (String interest : userInterests) {
                        if (description.contains(interest)) {
                            eventWithScore.incrementScore(2);
                        }
                    }

                    return eventWithScore;
                })
                .sorted((e1, e2) -> Integer.compare(e2.getScore(), e1.getScore()))
                .map(eventWithScore -> {
                    EventResponse response = new EventResponse(eventWithScore.getEvent());
                    response.setRecommended(eventWithScore.getScore() > 5);
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getPastEventCategoryFrequency(Long userId) {
        LocalDate today = LocalDate.now();

        // Kullanıcının katıldığı ve tarihi geçmiş etkinlikleri al
        List<Event> pastEvents = eventRepository.findPastEventsByParticipantId(userId, today);

        // Kategori frekanslarını hesapla
        return pastEvents.stream()
                .map(event -> event.getCategory().toLowerCase())
                .collect(Collectors.groupingBy(
                        category -> category,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    public Event createEvent(EventCreateRequest eventCreateRequest, Long userId) {
        // Kullanıcıyı al
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Yeni etkinliğin başlangıç ve bitiş zamanını hesapla
        LocalDateTime newEventStart = LocalDateTime.of(eventCreateRequest.getDate(), eventCreateRequest.getTime());
        LocalDateTime newEventEnd = newEventStart.plusMinutes(eventCreateRequest.getDuration());

        // Kullanıcı tarafından aynı tarihte oluşturulan etkinlikleri getir
        List<Event> userEvents = eventRepository.findEventsByCreatorAndDate(userId, eventCreateRequest.getDate());

        for (Event userEvent : userEvents) {
            LocalDateTime userEventStart = LocalDateTime.of(userEvent.getDate(), userEvent.getTime());
            LocalDateTime userEventEnd = userEventStart.plusMinutes(userEvent.getDuration());

            // Çakışma kontrolü
            if (isOverlapping(newEventStart, newEventEnd, userEventStart, userEventEnd)) {
                throw new RuntimeException("Bu saatlerde başka bir etkinlik oluşturulmuş.");
            }
        }
        // Yeni etkinliği oluştur
        Event event = new Event();
        event.setCreator(user);
        event.setEventName(eventCreateRequest.getEventName());
        event.setDescription(eventCreateRequest.getDescription());
        event.setDate(eventCreateRequest.getDate());
        event.setTime(eventCreateRequest.getTime());
        event.setDuration(eventCreateRequest.getDuration());
        event.setLocation(eventCreateRequest.getLocation());
        event.setCategory(eventCreateRequest.getCategory());

        // Kullanıcıyı katılımcı olarak ekle
        event.getParticipants().add(user);

        // Etkinliği kaydet
        Event savedEvent = eventRepository.save(event);

        // Kullanıcıya etkinlik oluşturma puanı ekle
        Point point = new Point();
        point.setUser(user);
        point.setScore(15); // Etkinlik oluşturma puanı
        point.setEarnedDate(new Date());
        pointRepository.save(point);

        return savedEvent;
    }
    
    public void joinEvent(Long eventId, Long userId) {
        // Etkinlik ve kullanıcı bilgilerini al
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Etkinlik bulunamadı: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userId));

        // Kullanıcının etkinliğe katılıp katılmadığını kontrol et
        if (event.getParticipants().contains(user)) {
            throw new IllegalArgumentException("Kullanıcı zaten bu etkinliğe katılmış.");
        }

        LocalDateTime eventStart = LocalDateTime.of(event.getDate(), event.getTime());
        LocalDateTime eventEnd = eventStart.plusMinutes(event.getDuration());

        // Kullanıcının katıldığı aynı tarihteki etkinlikleri getir
        List<Event> userEvents = eventRepository.findUserEventsByDate(userId, event.getDate());

        for (Event userEvent : userEvents) {
            LocalDateTime userEventStart = LocalDateTime.of(userEvent.getDate(), userEvent.getTime());
            LocalDateTime userEventEnd = userEventStart.plusMinutes(userEvent.getDuration());

            if (isOverlapping(eventStart, eventEnd, userEventStart, userEventEnd)) {
                throw new IllegalArgumentException("Kullanıcı bu saat aralığında başka bir etkinliğe katılmış.");
            }
        }

        // Kullanıcıyı etkinliğe ekle
        event.getParticipants().add(user);
        eventRepository.save(event);

        // Puan ekleme mantığı
        boolean isFirstParticipation = pointRepository.countByUser(user) == 0; // İlk katılım kontrolü
        int scoreToAdd = isFirstParticipation ? 20 : 10;

        Point point = new Point();
        point.setUser(user);
        point.setEarnedDate(new Date());
        point.setScore(scoreToAdd);

        pointRepository.save(point);
    }

    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    public EventResponse getEventById(Long eventId)
    {
        Event event = eventRepository.findById(eventId).orElse(null);
        assert event != null;
        return new EventResponse(event);
    }

    public Event getEventMessageById(Long eventId)
    {
        Event event = eventRepository.findById(eventId).orElse(null);
        assert event != null;
        return event;
    }

    public List<EventResponse> getJoinedEventsByUserId(Long userId) {
        // Kullanıcının oluşturduğu etkinlikleri hariç tutarak katıldığı etkinlikleri alıyoruz
        List<Event> list = eventRepository.findByParticipants_Id(userId).stream()
                .filter(event -> event.getCreator() != null && !event.getCreator().getId().equals(userId)) // Creator null değilse kontrol et
                .collect(Collectors.toList());
        return list.stream().map(e -> new EventResponse(e)).collect(Collectors.toList());
    }

    public List<EventResponse> getCreatedEventsByUserId(Long userId) {
        // Kullanıcının oluşturduğu etkinlikleri alıyoruz
        List<Event> createdEvents = eventRepository.findByCreator_Id(userId);

        // EventResponse nesnesine dönüştürüyoruz
        return createdEvents.stream()
                .map(EventResponse::new) // Lambda yerine direkt constructor referansı
                .collect(Collectors.toList());
    }

    public Event updateEventById(Long eventId, EventUpdateRequest eventUpdateRequest)
    {
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isPresent())
        {
            Event foundEvent = event.get();
            foundEvent.setEventName(eventUpdateRequest.getEventName());
            foundEvent.setDate(eventUpdateRequest.getDate());
            foundEvent.setDescription(eventUpdateRequest.getDescription());
            foundEvent.setLocation(eventUpdateRequest.getLocation());
            foundEvent.setDuration(eventUpdateRequest.getDuration());
            foundEvent.setCategory(eventUpdateRequest.getCategory());
            foundEvent.setTime(eventUpdateRequest.getTime());
            return eventRepository.save(foundEvent);
        }
        else
            return null;
    }

    public void deleteEventById(Long eventId)
    {
        eventRepository.deleteById(eventId);
    }

}
