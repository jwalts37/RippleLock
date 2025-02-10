package com.nanoha.MyLockScreen_all;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import com.nanoha.util.ManageKeyguard;
import com.nanoha.util.Util;

public class MyLockService extends Service {
    boolean isModeOne = true;
    KeyguardManager.KeyguardLock kl;
    KeyguardManager km;
    ScreenReceiver receiver;
    Util util;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Util util2 = Util.getInstance(this);
        int fixInt = util2.getFixDoubleLockIssue();
        Settings.System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 0);
        if (!util2.isUseSecurityLock() && fixInt != 3) {
            this.km = (KeyguardManager) getSystemService("keyguard");
            this.kl = this.km.newKeyguardLock("ripplelock");
            this.kl.disableKeyguard();
        }
        this.receiver = new ScreenReceiver();
        registReceiver();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, filter);
    }

    public void unregistReceiver() {
        if (this.receiver != null) {
            try {
                unregisterReceiver(this.receiver);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.receiver = null;
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.kl != null) {
            this.kl.reenableKeyguard();
        }
        ManageKeyguard.reenableKeyguard();
        unregistReceiver();
    }
}
