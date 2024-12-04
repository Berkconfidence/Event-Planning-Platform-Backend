package com.project.request;

import java.util.Date;

public class PointAddRequest {

    private Long pointId;
    private int score;
    private Date earnedDate;
    private Long userId;

    public PointAddRequest(Long pointId, int score, Date earnedDate, Long userId) {
        this.pointId = pointId;
        this.score = score;
        this.earnedDate = earnedDate;
        this.userId = userId;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
