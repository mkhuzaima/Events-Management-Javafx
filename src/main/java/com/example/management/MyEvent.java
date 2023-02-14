package com.example.management;

import java.time.LocalDate;

public class MyEvent {

    static int idCounter = 0;
    private int id = -1;
    private String name;
    private LocalDate date;
    private int hours;
    private int minutes;
    private String venue;

    public MyEvent(String name, LocalDate date, int hours, int minutes, String venue) {
//        this.name = name;
//        this.date = date;
//        this.hours = hours;
//        this.minutes = minutes;
//        this.venue = venue;
//        this.id = idCounter++;

        this(name, date, hours, minutes, venue, idCounter++);
    }
    public MyEvent(String name, LocalDate date, int hours, int minutes, String venue, int id) {
        this.name = name;
        this.date = date;
        this.hours = hours;
        this.minutes = minutes;
        this.venue = venue;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
