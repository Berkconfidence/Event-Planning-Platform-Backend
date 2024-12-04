package com.project.repository;

import com.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entities.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUserId(Long userId);
    @Query("SELECT COUNT(p) FROM Point p WHERE p.user = :user")
    int countByUser(@Param("user") User user);
}
