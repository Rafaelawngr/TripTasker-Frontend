package com.triptasker.myapplication;

public class Trip {
    private int id;
    private String title;

    // Construtor com parâmetros
    public Trip(int id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
