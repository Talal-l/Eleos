package com.c50x.eleos.data;


public class GameRequest extends Request {

    private int gameId;
    private String teamName;
    private String challengedTeam;
    private String time;
    private String date;
    private String venue;


    public GameRequest(Game game, String receiver, int state) {
        this.gameId = game.getGameId();

        this.setSender(game.getGameAdmin());
        this.setReceiver(receiver);
        this.setState(state);
        this.setType("GameRequest");
        this.setTitle("Game Invitation");

        this.teamName = game.getTeam1();
        this.challengedTeam = game.getTeam2();
        this.time = game.getStartTime();
        this.date = game.getStartDate();
        this.venue = game.getVenueAddress();

    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getChallengedTeam() {
        return challengedTeam;
    }

    public void setChallengedTeam(String challengedTeam) {
        this.challengedTeam = challengedTeam;
    }

    public String getDateTime() {

        // TODO: better format
        String formatedDateTime = date + " " + time;

        return formatedDateTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
