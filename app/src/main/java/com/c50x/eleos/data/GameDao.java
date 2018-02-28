package com.c50x.eleos.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;



@Dao
public interface GameDao {

    @Insert
    Long addGame(Game game);

    @Update
    Long modifyGame(Game game);

    @Delete
    Long deleteGame(Game game);

    @Query("SELECT * FROM Game WHERE gameId LIKE:id")
    Game[] loadGame(int id);

    @Query("SELECT * FROM GAME")
    Game [] LoadAllGames();

}


