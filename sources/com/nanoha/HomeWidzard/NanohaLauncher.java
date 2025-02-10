package com.nanoha.HomeWidzard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.nanoha.MyLockScreen_all.CircleLockScreen;
import com.nanoha.util.Util;

public class NanohaLauncher extends Activity {
    Util util;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.util = Util.getInstance(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.util.isLocked()) {
            Intent mainActivity = new Intent(this, CircleLockScreen.class);
            mainActivity.addFlags(67108864);
            mainActivity.addFlags(65536);
            startActivity(mainActivity);
        } else if (this.util.getValue(Util.IN_HOME_WIDZARD, false)) {
            startActivity(new Intent(this, HomeWidzardTwo.class));
        } else {
            String packageName = this.util.getValue(Util.USE_LAUNCHER_PACKAGE_NAME, (String) null);
            String className = this.util.getValue(Util.USE_LAUNCHER_CLASS_NAME, (String) null);
            if (packageName == null || className == null) {
                showHomeWidzardTwo();
                return;
            }
            Intent localIntent1 = new Intent();
            localIntent1.setComponent(new ComponentName(packageName, className));
            localIntent1.addCategory("");
            localIntent1.setAction("");
            localIntent1.addFlags(8388608);
            localIntent1.addFlags(4194304);
            Intent localIntent7 = new Intent();
            localIntent7.setAction("android.intent.action.MAIN");
            localIntent7.addCategory("android.intent.category.HOME");
            localIntent7.setComponent(new ComponentName(packageName, className));
            localIntent7.addFlags(8388608);
            try {
                if (packageName.equals(((ActivityManager) getSystemService("activity")).getRunningTasks(2).get(1).topActivity.getPackageName())) {
                    startActivity(localIntent1);
                }
                startActivity(localIntent7);
            } catch (Exception e) {
            }
        }
    }

    private void showHomeWidzardTwo() {
        startActivity(new Intent(this, HomeWidzardTwo.class));
    }
}
