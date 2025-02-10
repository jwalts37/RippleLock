package com.nanoha.smartClock;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.nanoha.MyLockScreen_all.R;

public class KeyguardUpdateMonitor {
    public static final String ALARM_CHANGE_ACTION = "android.intent.action.ALARM_CHANGED";
    public static final String EXTRA_PLMN = "plmn";
    public static final String EXTRA_SHOW_PLMN = "showPlmn";
    public static final String EXTRA_SHOW_SPN = "showSpn";
    public static final String EXTRA_SPN = "spn";
    private static final int MSG_ALARM_CHANGE = 304;
    private static final int MSG_BATTERY_UPDATE = 302;
    private static final int MSG_CARRIER_INFO_UPDATE = 303;
    public static final String SPN_STRINGS_UPDATED_ACTION = "android.provider.Telephony.SPN_STRINGS_UPDATED";
    private static KeyguardUpdateMonitor instance;
    private BroadcastReceiver broadcaseReceiver;
    private final IntentFilter filter;
    private int mBatteryLevel;
    private Context mContext;
    private boolean mDevicePluggedIn;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KeyguardUpdateMonitor.MSG_BATTERY_UPDATE /*302*/:
                    KeyguardUpdateMonitor.this.handleBatteryUpdate(msg.arg1, msg.arg2);
                    return;
                case KeyguardUpdateMonitor.MSG_CARRIER_INFO_UPDATE /*303*/:
                    KeyguardUpdateMonitor.this.handleCarrierInfoUpdate();
                    return;
                case KeyguardUpdateMonitor.MSG_ALARM_CHANGE /*304*/:
                    KeyguardUpdateMonitor.this.handleAlarmUpdate();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public CharSequence mTelephonyPlmn;
    /* access modifiers changed from: private */
    public CharSequence mTelephonySpn;

    public static KeyguardUpdateMonitor getInstance(Context context) {
        if (instance == null) {
            instance = new KeyguardUpdateMonitor(context);
        }
        return instance;
    }

    private boolean isPluggedIn(int status) {
        return status == 2 || status == 5;
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    public boolean getPlugIn() {
        return this.mDevicePluggedIn;
    }

    private void updateWidgetInfo(int updateWidgetFrom) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this.mContext);
        for (int valueOf : manager.getAppWidgetIds(new ComponentName(this.mContext, WidgetProvider.class))) {
            WidgetProvider.updateAppWidget(this.mContext, manager, Integer.valueOf(valueOf), updateWidgetFrom);
        }
    }

    /* access modifiers changed from: private */
    public void handleCarrierInfoUpdate() {
        updateWidgetInfo(1003);
    }

    /* access modifiers changed from: private */
    public void handleAlarmUpdate() {
        updateWidgetInfo(Util.UPDATE_WIDGET_FROM_ALARM);
    }

    /* access modifiers changed from: private */
    public void handleBatteryUpdate(int pluggedInStatus, int batteryLevel) {
        boolean pluggedIn = isPluggedIn(pluggedInStatus);
        this.mBatteryLevel = batteryLevel;
        this.mDevicePluggedIn = pluggedIn;
        updateWidgetInfo(Util.UPDATE_WIDGET_FROM_CHARGEINFO);
    }

    private KeyguardUpdateMonitor(Context context) {
        this.mContext = context;
        this.mBatteryLevel = 100;
        this.mTelephonyPlmn = getDefaultPlmn();
        this.filter = new IntentFilter();
        this.filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.filter.addAction("android.provider.Telephony.SPN_STRINGS_UPDATED");
        this.filter.addAction(ALARM_CHANGE_ACTION);
        this.broadcaseReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.provider.Telephony.SPN_STRINGS_UPDATED".equals(action)) {
                    KeyguardUpdateMonitor.this.mTelephonyPlmn = KeyguardUpdateMonitor.this.getTelephonyPlmnFrom(intent);
                    KeyguardUpdateMonitor.this.mTelephonySpn = KeyguardUpdateMonitor.this.getTelephonySpnFrom(intent);
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_CARRIER_INFO_UPDATE);
                } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_BATTERY_UPDATE, intent.getIntExtra("status", 1), intent.getIntExtra("level", 0)));
                } else if (KeyguardUpdateMonitor.ALARM_CHANGE_ACTION.equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_ALARM_CHANGE);
                }
            }
        };
    }

    public void initReceiver(Context context) {
        try {
            context.registerReceiver(this.broadcaseReceiver, this.filter);
        } catch (Exception e) {
        }
    }

    public void unregistReceiver() {
        try {
            if (this.broadcaseReceiver != null) {
                this.mContext.unregisterReceiver(this.broadcaseReceiver);
                this.broadcaseReceiver = null;
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public CharSequence getTelephonyPlmnFrom(Intent intent) {
        if (!intent.getBooleanExtra("showPlmn", false)) {
            return null;
        }
        String plmn = intent.getStringExtra("plmn");
        if (plmn != null) {
            return plmn;
        }
        return getDefaultPlmn();
    }

    /* access modifiers changed from: private */
    public CharSequence getTelephonySpnFrom(Intent intent) {
        String spn;
        if (!intent.getBooleanExtra("showSpn", false) || (spn = intent.getStringExtra("spn")) == null) {
            return null;
        }
        return spn;
    }

    public CharSequence getTelephonyPlmn() {
        return this.mTelephonyPlmn;
    }

    public CharSequence getTelephonySpn() {
        return this.mTelephonySpn;
    }

    private CharSequence getDefaultPlmn() {
        return this.mContext.getResources().getText(R.string.carrier_default);
    }
}
