package com.nanoha.MyLockScreen_all;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.LinearLayout;

public class LockScreenManager {
    private Configuration mConfiguration;
    private Context mContext;
    private final KeyguardUpdateMonitor mUpdateMonitor;

    public LockScreenManager(Context context) {
        this.mContext = context;
        this.mConfiguration = context.getResources().getConfiguration();
        this.mUpdateMonitor = new KeyguardUpdateMonitor(context);
    }

    public LockScreen createLockscreen() {
        return new LockScreen(this.mContext, this.mConfiguration, this.mUpdateMonitor, false);
    }

    public LinearLayout createSelectWidgetLockScreen(WidgetLayout widgetLayout) {
        return new LockScreen(this.mContext, this.mConfiguration, this.mUpdateMonitor, true);
    }
}
