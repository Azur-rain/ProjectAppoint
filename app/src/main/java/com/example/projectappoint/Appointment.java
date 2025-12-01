package com.example.projectappoint;


import java.util.Calendar;

public class Appointment {
    private String date;
    private String time;
    private boolean isSelected;
    private Calendar calendar;
    private String dayOfWeek;

    public Appointment(String date, String time, Calendar calendar, String dayOfWeek) {
        this.date = date;
        this.time = time;
        this.isSelected = false;
        this.calendar = calendar;
        this.dayOfWeek = dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}