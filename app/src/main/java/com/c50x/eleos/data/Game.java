package com.c50x.eleos.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {
        @ForeignKey(entity = Team.class,
                parentColumns = "teamName",
                childColumns = "team1",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(entity = Team.class,
                parentColumns = "teamName",
                childColumns =  "team2",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE

        ),
                @ForeignKey(entity = Venue.class,
                parentColumns = "venueAddress",
                childColumns =  "venueAddress",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE

        )
})

public class Game {

    @PrimaryKey
    @NonNull
    private int gameId;

    private int team1Score;
    private int team2Score;
    private double duration;
    private String startTime;
    private String startDate;
    private int ratting;
    private String sport;

    // Foreign keys
    private String venueAddress;
    private String team1;
    private String team2;


    @NonNull
    public int getGameId() {
        return gameId;
    }

    public void setGameId(@NonNull int gameId) {
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

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
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
}
