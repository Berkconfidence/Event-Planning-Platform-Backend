package com.project.responses;
import com.project.entities.Message;

import java.util.Date;

public class MessageResponse {
    private Long messageID;
    private String username;
    private String messageText;
    private Date sentTime;

    public MessageResponse(Long messageID, String username, String messageText, Date sentTime) {
        this.messageID = messageID;
        this.username = username;
        this.messageText = messageText;
        this.sentTime = sentTime;
    }

    public static MessageResponse fromMessage(Message message) {
        return new MessageResponse(
                message.getMessageID(),
                message.getSender().getUsername(),
                message.getMessageText(),
                message.getSentTime()
        );
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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