package com.kna.image2pdfmvp;

import android.app.Application;

import androidx.room.RoomDatabase;

public class App extends Application {
    private static App instance;
    private RoomDatabase database;

    public static App getInstance() {
        return instance;
    }

    public RoomDatabase getDatabase() {
        return database;
    }
}
