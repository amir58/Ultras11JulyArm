package com.amirmohammed.ultras11july2;

public class Task {

    private Long id;

    private String title;

    private String date;

    private String time;

    private String status = "active";

    public Task(Long id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStatus() {
        return status;
    }

    public void setStatusDone() {
        this.status = "done";
    }

     public void setStatusArchive() {
        this.status = "archive";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
