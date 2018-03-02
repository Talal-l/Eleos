package com.c50x.eleos.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface TeamDao{

    @Insert
    Long addTeam(Team team);

    @Update
    int modifyTeam(Team team);

    @Delete
    int deleteTeam(Team team);

    @Query("SELECT * FROM TEAM WHERE teamName LIKE :teamName")
    Team [] loadTeam(String teamName);

    @Query("SELECT * FROM TEAM")
    Team [] LoadAllTeams();
}

