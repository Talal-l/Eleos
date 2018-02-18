package com.c50x.eleos.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;
    public abstract UserDao userDao();

    public static AppDatabase getDatabaseInstance(Context context) { // Create a database if it doesn't exist
        if (INSTANCE == null) {
            // Create the database and call it eleos.db
            INSTANCE =
                    Room.databaseBuilder(context,AppDatabase.class,"eleos.db").build();
        }
        return INSTANCE;
    }}
