package com.project.services;

import com.project.entities.Event;
import com.project.responses.EventResponse;
import org.springframework.stereotype.Service;
import com.project.entities.Message;
import com.project.entities.User;
import com.project.repository.MessageRepository;
import com.project.request.MessageCreateRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private UserService userService;
    private EventService eventService;

    public MessageService(MessageRepository messageRepository, UserService userService, EventService eventService)
    {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByEventId(Long eventId){
        return messageRepository.findByEvent_EventIDOrderBySentTimeDesc(eventId);
    }

    public Message addMessage(Long eventId, MessageCreateRequest messageCreateRequest) {
        User sender = userService.getUserById(messageCreateRequest.getSenderId());
        if(sender == null) {
            throw new RuntimeException("Kullanıcı bulunamadı");
        }

        Event event = eventService.getEventMessageById(eventId);
        if(event == null) {
            throw new RuntimeException("Etkinlik bulunamadı");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setEvent(event);
        message.setMessageText(messageCreateRequest.getMessageText());

        // Şu anki tarih ve saati al
        LocalDateTime now = LocalDateTime.now();
        // TimeZone'u Türkiye'ye ayarla
        ZoneId turkeyZone = ZoneId.of("Europe/Istanbul");
        LocalDateTime turkeyTime = LocalDateTime.now(turkeyZone);
        message.setSentTime(Date.from(turkeyTime.atZone(turkeyZone).toInstant()));

        Message savedMessage = messageRepository.save(message);
        return messageRepository.findById(savedMessage.getMessageID())
                .orElseThrow(() -> new RuntimeException("Mesaj kaydedilemedi"));
    }

    public Message getMessageById(Long messageId)
    {
        return messageRepository.findById(messageId).orElse(null);
    }

    public List<Message> getMessageByUserId(Optional<Long> userId) {
        if(userId.isPresent())
            return messageRepository.findBySender_Id(userId.get()); // receive sender ?
        else
            return messageRepository.findAll();
    }

    public void deleteMessageById(Long messageId)
    {
        messageRepository.deleteById(messageId);
    }
}