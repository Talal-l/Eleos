package com.c50x.eleos.models;

import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;

public class RvRequestModel {

    private String message;



    public RvRequestModel(String message) {
        this.message = message;
    }

    // if the request is for a game
    public RvRequestModel(String gameAdmin, String mainTeam, String challengedTeam) {

        this.message = gameAdmin + "is asking for a challenge between " + mainTeam + " and "
                + challengedTeam;

    }

    // if the request is for a team
    public RvRequestModel(String teamName, String teamAdmin) {

        this.message = teamAdmin + " has invited you to join the " + teamName + " team.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
