package com.triptasker.myapplication;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private String date;
    private String status;

    public Task(int taskId, String name, String description, String date, String status) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getTaskId() { return taskId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}
