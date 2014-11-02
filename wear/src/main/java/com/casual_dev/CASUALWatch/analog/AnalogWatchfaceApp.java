package com.casual_dev.CASUALWatch.analog;

import com.twotoasters.watchface.gears.GearsWatchfaceApp;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class AnalogWatchfaceApp extends GearsWatchfaceApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new DebugTree());
    }
}
