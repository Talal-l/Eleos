package com.c50x.eleos.models;

import com.c50x.eleos.data.Game;
import com.c50x.eleos.data.Team;

public class RvRequestModel {

    private String message;



    public RvRequestModel(String message) {
        this.message = message;
    }

    // if the request is for a game
    public RvRequestModel(Game game) {

        String mainTeam = game.getTeam1();
        String challangedTeam = game.getTeam2();
        String gameAdmin = game.getGameAdmin();


        this.message = gameAdmin + "is asking for a challenge between " + mainTeam + " and "
                + challangedTeam;

    }

    // if the request is for a team
    public RvRequestModel(Team team) {
        String teamName = team.getTeamName();
        String teamAdmin = team.getTeamAdmin();

        this.message = teamAdmin + " has invited you to join the " + teamName + " team.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
