package com.project.repository;

import com.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entities.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByParticipants_Id(Long userId);
    List<Event> findByCreator_Id(Long userId);
    @Query("SELECT e FROM Event e JOIN e.participants p " +
            "WHERE p.userID = :userId AND e.date < :today")
    List<Event> findPastEventsByParticipantId(@Param("userId") Long userId, @Param("today") LocalDate today);
    @Query("SELECT e FROM Event e WHERE e.eventID NOT IN " +
            "(SELECT e2.eventID FROM Event e2 JOIN e2.participants p WHERE p.userID = :userId)")
    List<Event> findEventsNotJoinedByUser(@Param("userId") Long userId);
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId AND e.date = :date")
    List<Event> findUserEventsByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    @Query("SELECT e FROM Event e WHERE e.creator.id = :creatorId AND e.date = :date")
    List<Event> findEventsByCreatorAndDate(@Param("creatorId") Long creatorId, @Param("date") LocalDate date);


}
