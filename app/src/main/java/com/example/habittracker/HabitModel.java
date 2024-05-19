package com.example.habittracker;

import java.util.Date;
import java.util.UUID;

public class HabitModel {
   String id;
    String name;
   String comment;
   long timerDuration; // Timer duration in milliseconds
    boolean isComplete;



    public HabitModel(String name, String comment) {
        this.id = generateId(); // Automatically generate a unique ID
        this.name = name;
        this.comment = comment;
        this.timerDuration = 0; // Default timer duration
        this.isComplete = false;
    }

    // Constructor including timerDuration
    public HabitModel(String name, String comment, long timerDuration) {
        this.id = generateId(); // Implement generateId() to create a unique ID or set it later
        this.name = name;
        this.comment = comment;
        this.timerDuration = timerDuration;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(long timerDuration) {
        this.timerDuration = timerDuration;
    }

    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }
}

