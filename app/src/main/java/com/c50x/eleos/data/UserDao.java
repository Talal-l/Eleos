package com.c50x.eleos.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long addUser(User user);

    @Query("SELECT * FROM User")
    User[] loadAllUsers();

    @Query("SELECT * FROM User WHERE handle LIKE:h ")
    User[] loadUserWithHandle(String h);

    @Query("SELECT * FROM User WHERE email LIKE:e ")
    User[] loadUserWithEmail(String e);
}

