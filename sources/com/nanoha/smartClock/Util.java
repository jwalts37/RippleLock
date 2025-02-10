package com.nanoha.smartClock;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

public class Util {
    public static String AM_STRING = "AM";
    public static final int DEFAULT_ALARM_SIZE = 16;
    public static final int DEFAULT_AMPM_SIZE = 15;
    public static final int DEFAULT_CARRIER_SIZE = 16;
    public static final int DEFAULT_CHARGEINFO_SIZE = 16;
    public static final int DEFAULT_DATE_SIZE = 16;
    public static final int DEFAULT_TIME_SIZE = 52;
    public static final String INTENT_ACTION_SETTING_CHANGE = "com.nanoha.smartclock.setting_change";
    public static final String KEY_ALARM_COLOR = "alarm_color";
    public static final String KEY_ALARM_SIZE = "alarm_size";
    public static final String KEY_ALL_WIDGETS = "all_widgets";
    public static final String KEY_AMPM_SIZE = "ampm_size";
    public static final String KEY_BOTTOM_AD_COUNT = "bottom_ad_count";
    public static final String KEY_CARRIER_COLOR = "carrier_color";
    public static final String KEY_CARRIER_SIZE = "carrier_size";
    public static final String KEY_CHARGEINFO_COLOR = "chargeinfo_color";
    public static final String KEY_CHARGEINFO_SIZE = "chargeinfo_size";
    public static final String KEY_DATE_COLOR = "date_color";
    public static final String KEY_DATE_FORMAT = "date_format";
    public static final String KEY_DATE_SIZE = "date_size";
    public static final String KEY_DISPLAY_AD = "display_ad";
    public static final String KEY_DISPLAY_ALARM = "display_alarm";
    public static final String KEY_DISPLAY_BOTTOM_AD = "display_bottom_ad";
    public static final String KEY_DISPLAY_CARRIER = "display_carrier";
    public static final String KEY_DISPLAY_CHARGEINFO = "display_chargeinfo";
    public static final String KEY_DISPLAY_DATE = "display_date";
    public static final String KEY_DISPLAY_TIME = "display_time";
    public static final String KEY_DISPLAY_TOP_AD = "display_top_ad";
    public static final String KEY_MAX_VERION = "max_version";
    public static final String KEY_REGIST_VERSION = "display_lock";
    public static final String KEY_TIME_COLOR = "time_color";
    public static final String KEY_TIME_FORMAT = "time_format";
    public static final String KEY_TIME_SIZE = "time_size";
    public static final String KEY_TOP_AD_COUNT = "top_ad_count";
    public static final String KEY_USE_24_HOUR = "use_24_hour";
    public static final String KEY_WIDGET_CLICK_ACTION = "widget_click_action";
    public static final String M12 = "h:mm";
    public static final String M24 = "kk:mm";
    public static String PM_STRING = "PM";
    public static final int REGIST_VALUE = 2004;
    public static final String SETTING_PREF = "clock_setting_pref";
    public static boolean START_ALARM_SERVICE = true;
    public static final int UPDATE_WIDGET_FROM_ALARM = 1005;
    public static final int UPDATE_WIDGET_FROM_CARRIER = 1003;
    public static final int UPDATE_WIDGET_FROM_CHARGEINFO = 1004;
    public static final int UPDATE_WIDGET_FROM_SETTING_CHANGE = 1006;
    public static final int UPDATE_WIDGET_FROM_SYSTEM = 1002;
    public static final int UPDATE_WIDGET_FROM_TIME = 1001;
    private static Util utilInstance;
    private BroadcastReceiver changeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int updateFrom = 1001;
            if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                updateFrom = 1001;
            } else if (Util.INTENT_ACTION_SETTING_CHANGE.equals(action)) {
                updateFrom = Util.UPDATE_WIDGET_FROM_SETTING_CHANGE;
            }
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            for (int valueOf : manager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class))) {
                WidgetProvider.updateAppWidget(context, manager, Integer.valueOf(valueOf), updateFrom);
            }
        }
    };
    private Context mContext;

    public void saveFontColor(String key, int color) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(key, color);
        editor.commit();
    }

    public void registFreeVersion() {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt("display_lock", 2004);
        editor.commit();
    }

    public boolean checkHasAd() {
        if (this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("display_lock", 0) == 2004) {
            return false;
        }
        return true;
    }

    public void addWidgetId(int[] widgetIds) {
    }

    public static Util getInstance(Context context) {
        if (utilInstance == null) {
            utilInstance = new Util(context);
        }
        return utilInstance;
    }

    public void updateClockWidget() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this.mContext);
        for (int valueOf : manager.getAppWidgetIds(new ComponentName(this.mContext, WidgetProvider.class))) {
            WidgetProvider.updateAppWidget(this.mContext, manager, Integer.valueOf(valueOf), 1002);
        }
    }

    public void registReceiver(Context context) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.TIME_TICK");
            filter.addAction("android.intent.action.TIME_SET");
            filter.addAction("android.intent.action.TIMEZONE_CHANGED");
            filter.addAction(INTENT_ACTION_SETTING_CHANGE);
            context.registerReceiver(this.changeReceiver, filter);
        } catch (Exception e) {
            Log.d("nanoha", "registReceiver error=" + e.toString());
        }
    }

    public void unregistReceiver(Context context) {
        try {
            context.unregisterReceiver(this.changeReceiver);
        } catch (Exception e) {
            Log.d("nanoha", "unregistReceiver error=" + e.toString());
        }
    }

    private Util(Context context) {
        this.mContext = context;
    }

    public void saveValue(String key, int value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getValue(String key, int defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(key, defaultValue);
    }

    public void saveValue(String key, String value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key, String defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(key, defaultValue);
    }

    public void saveValue(String key, boolean value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getValue(String key, boolean defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(key, defaultValue);
    }
}
