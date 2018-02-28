package com.c50x.eleos.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PlayerTeamMap {

    @PrimaryKey
    @NonNull
    private String playerHandle;
    private String teamName;

    @NonNull
    public String getPlayerHandle() {
        return playerHandle;
    }

    public void setPlayerHandle(@NonNull String playerHandle) {
        this.playerHandle = playerHandle;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
