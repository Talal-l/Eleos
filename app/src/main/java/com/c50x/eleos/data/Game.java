package com.c50x.eleos.data;

import static com.c50x.eleos.data.Request.PENDING;

public class Game {

    public static final int CASUAL = 0;
    public static final int COMPETITIVE = 1;
    private int gameId;
    private int state;
    private String refereeHandle;
    private int type;
    private String gameName;
    private int team1Score;
    private int team2Score;
    private double duration;
    private String startTime;
    private String startDate;
    private int ratting;
    private String sport;
    private String gameAdmin;
    private String[] gamePlayers;

    // Foreign keys
    private Venue venue;

    private String team1;
    private String team2;

    public Game() {
        gameId = 0;
        gameAdmin = "";
        team1 = "";
        team2 = "";
        team1Score = 0;
        team2Score = 0;
        ratting = 0;
        sport = "";
        venue = new Venue();
        startDate = "";
        startTime = "";
        state = PENDING;
        refereeHandle = null;
        type = CASUAL;


    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getGameAdmin() {
        return gameAdmin;
    }

    public void setGameAdmin(String gameAdmin) {
        this.gameAdmin = gameAdmin;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String[] getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(String[] gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRefereeHandle() {
        return refereeHandle;
    }

    public void setRefereeHandle(String refereeHandle) {
        this.refereeHandle = refereeHandle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDateTime() {
        // TODO: parse into a better format
        String formatedDateTime = startDate + " " + startTime;
        return formatedDateTime;
    }
}
