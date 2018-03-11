package com.c50x.eleos.models;

import com.c50x.eleos.data.Game;

public class RvGameModel {

    private String title;
    private String message;
    private int gameid;



    public RvGameModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public RvGameModel(Game game) {
        this.title = game.getGameName();
        this.message = game.getGameAdmin();
        this.gameid = game.getGameId();
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

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
}
