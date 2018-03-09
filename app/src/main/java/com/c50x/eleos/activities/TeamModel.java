package com.c50x.eleos.activities;

import java.util.ArrayList;

public class TeamModel {

    private String title;

    private String message;


    public TeamModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public TeamModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
