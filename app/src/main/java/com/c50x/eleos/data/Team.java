package com.c50x.eleos.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Team {

    @PrimaryKey
    @NonNull
    private String teamName;
    private String sport;
    private String teamAdmin;

    @NonNull
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(@NonNull String teamName) {
        this.teamName = teamName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(String teamAdmin) {
        this.teamAdmin = teamAdmin;
    }
}
