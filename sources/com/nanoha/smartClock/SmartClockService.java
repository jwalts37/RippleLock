package com.nanoha.smartClock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SmartClockService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("nanoha", "service onCreate");
        Util.getInstance(this).registReceiver(this);
        KeyguardUpdateMonitor.getInstance(getApplicationContext()).initReceiver(getApplicationContext());
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        Log.d("nanoha", "service onDestroy");
        super.onDestroy();
        Util.getInstance(this).unregistReceiver(this);
        KeyguardUpdateMonitor.getInstance(getApplicationContext()).unregistReceiver();
    }
}
