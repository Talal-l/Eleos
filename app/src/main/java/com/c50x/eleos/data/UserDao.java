package com.c50x.eleos.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long addUser(User user);
    @Query("SELECT * FROM user")
    List<User> loadAllUsers();
}

