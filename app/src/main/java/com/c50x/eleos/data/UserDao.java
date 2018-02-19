package com.c50x.eleos.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long addUser(User user);

    @Query("SELECT * FROM User")
    List<User> loadAllUsers();

    @Query("SELECT * FROM User WHERE handle=:h ")
    User loadUserWithHandle(String h);

    @Query("SELECT * FROM User WHERE email=:e ")
    User loadUserWithEmail(String e);


}

