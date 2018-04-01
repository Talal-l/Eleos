package com.c50x.eleos.data;

public class Request {
    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int DECLINED = 2;

    private int gameId;
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

    public Request(int gameId, String sender, String receiver, int state) {
        this.gameId = gameId;
        this.teamName = teamName;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
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
}
