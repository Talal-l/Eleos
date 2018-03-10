package com.c50x.eleos.models;

import java.util.ArrayList;

public class RvTeamModel {

    private String title;

    private String message;


    public RvTeamModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public RvTeamModel() {

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
