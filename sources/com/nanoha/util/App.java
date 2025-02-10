package com.nanoha.util;

import android.app.Application;

public class App extends Application {
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        crashHandler.sendPreviousReportsToServer();
    }
}
