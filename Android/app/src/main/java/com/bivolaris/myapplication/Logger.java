package com.bivolaris.myapplication;

import android.app.Application;

import timber.log.Timber;

public class Logger extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant();
    }
}

