package com.project.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false) // Gönderici ID'si foreign key olarak ekleniyor
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender; // Gönderen kullanıcı

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", nullable = true) // Alıcı ID'si foreign key olarak ekleniyor
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver; // Alıcı kullanıcı

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @Column(nullable = false)
    private String messageText;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentTime;

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Long getId() {
        return messageID;
    }

    public void setId(Long messageID) {
        this.messageID = messageID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
