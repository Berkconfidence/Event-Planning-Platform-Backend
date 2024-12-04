package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entities.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender_Id(Long senderId);
    List<Message> findByReceiver_Id(Long receiverId);
    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.event.eventID = :eventId ORDER BY m.sentTime ASC")
    List<Message> findByEvent_EventIDOrderBySentTimeDesc(@Param("eventId") Long eventId);
}
