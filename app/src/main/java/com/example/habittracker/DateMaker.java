package com.example.habittracker;

import java.util.Calendar;

public class DateMaker {
    private int day, month;
    private String monthName;

    Calendar now = Calendar.getInstance();


    //current date
    public DateMaker() {
        day = now.get(Calendar.DAY_OF_MONTH);
        month = now.get(Calendar.MONTH);
    }

    private String getCurrentMonth() {
        switch(month) {
            case 0:
                monthName = "Jan.";
                break;
            case 1:
                monthName = "Feb.";
                break;
            case 2:
                monthName = "Mar.";
                break;
            case 3:
                monthName = "Apr.";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "Jun.";
                break;
            case 6:
                monthName = "Jul.";
                break;
            case 7:
                monthName = "Aug.";
                break;
            case 8:
                monthName = "Sept.";
                break;
            case 9:
                monthName = "Oct.";
                break;
            case 10:
                monthName = "Nov.";
                break;
            case 11:
                monthName = "Dec.";
                break;
            default:
                monthName = "Welcome";
                break;
        }
        return monthName;
    }

    @Override
    public String toString() {
        return getCurrentMonth() + " " + day;
    }
}