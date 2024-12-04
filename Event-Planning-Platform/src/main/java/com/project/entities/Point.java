package com.project.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointID;

    private int score;
    private Date earnedDate;

    @ManyToOne
    @JoinColumn(name = "user_id") // Kullanıcı ID'si foreign key olarak ekleniyor
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    // Getter ve Setter metotları
    public Long getId() {
        return pointID;
    }

    public void setId(Long id) {
        this.pointID = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getEarnedDate() {
        return earnedDate;
    }

    public void setEarnedDate(Date earnedDate) {
        this.earnedDate = earnedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
