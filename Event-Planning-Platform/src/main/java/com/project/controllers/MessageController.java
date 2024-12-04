package com.project.controllers;

import com.project.responses.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.entities.Message;
import com.project.request.MessageCreateRequest;
import com.project.services.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;
    public MessageController(MessageService messageService)
    {
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByEventId(@PathVariable Long eventId) {
        List<Message> messages = messageService.getMessagesByEventId(eventId);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Message> addMessage(@PathVariable Long eventId, @RequestBody MessageCreateRequest messageRequest) {
        Message savedMessage = messageService.addMessage(eventId, messageRequest);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/{messageId}")
    public Message getMessageById(@PathVariable Long messageId)
    {
        return messageService.getMessageById(messageId);
    }

    @GetMapping("/user/{userId}")
    public List<Message> getMessageByUserId(@PathVariable Long userId)
    {
        return messageService.getMessageByUserId(Optional.of(userId));
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessageById(@PathVariable Long messageId)
    {
        messageService.deleteMessageById(messageId);
    }
}
