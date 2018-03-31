package com.c50x.eleos.models;

import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;

public class RvRequestModel {

    private String title;
    private String message;



    public RvRequestModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public RvRequestModel(Game game) {

    }
    public RvRequestModel(Team team) {
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
