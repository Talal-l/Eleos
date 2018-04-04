package com.c50x.eleos.data;

public class Request {
    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int DECLINED = 2;
    public static final int CANCELED = 3;

    private int requestId;
    private int gameId;
    private String challengedTeam;
    private String teamName;
    private String sender;
    private String receiver;
    private int state;

    public Request(String teamName, String sender, String receiver, int state) {
        this.gameId = -1;
        this.teamName = teamName;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
    }

    public Request(int gameId, String gameTeam1, String gameTeam2, String sender, String receiver, int state) {
        this.gameId = gameId;
        this.teamName = gameTeam1;
        this.challengedTeam = gameTeam2;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
    }

    public Request() {
        this.gameId = -1;
        this.challengedTeam = null;
        this.sender = null;
        this.receiver = null;
        this.state = -1;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getChallengedTeam() {
        return challengedTeam;
    }

    public void setChallengedTeam(String challengedTeam) {
        this.challengedTeam = challengedTeam;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
