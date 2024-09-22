package com.triptasker.myapplication;

import com.google.gson.annotations.SerializedName;

public class Trip {
    @SerializedName("TripId")
    private int id;

    @SerializedName("Title")
    private String title;

    public Trip() {
        // Construtor vazio para Gson
    }

    public Trip(int id, String title) {
        this.id = id;
        this.title = title;
    }

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
