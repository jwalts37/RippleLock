package com.nanoha.MyLockScreen_all;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.nanoha.util.Util;

public class StartBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Util.getInstance(context).isBootupAutoExecute() && context.getSharedPreferences(MyLockScreen.MYLOCKSCREEN_PREF, 2).getInt(MyLockScreen.PREF_STATE, 2) == 1) {
            context.startService(new Intent(context, MyLockService.class));
        }
    }
}
