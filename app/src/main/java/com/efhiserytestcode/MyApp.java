package com.efhiserytestcode;

import android.app.Application;

import androidx.room.Room;

import com.efhiserytestcode.database.AppDB;

public class MyApp extends Application {

    public static AppDB dbApp;

    @Override
    public void onCreate() {
        super.onCreate();

        // APP DB
        dbApp = Room.databaseBuilder(getApplicationContext(),
                AppDB.class,"list").allowMainThreadQueries().build();
    }
}
