package com.triptasker.myapplication;

public class Task {
    private String name;
    private String description;
    private String date;
    private String status;
    public Task(String name, String description, String date, String status) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}
