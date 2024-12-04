package com.project.request;

import java.util.Date;

public class PointUpdateRequest {

    private int score;
    private Date earnedDate;

    public PointUpdateRequest(int score, Date earnedDate) {
        this.score = score;
        this.earnedDate = earnedDate;
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
}
