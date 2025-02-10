package com.nanoha.smartClock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import com.nanoha.MyLockScreen_all.R;
import java.util.Calendar;
import java.util.Date;

public class WidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, SmartClockService.class));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, Integer.valueOf(appWidgetId), 1002);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    private void init(Context context) {
        context.startService(new Intent(context, SmartClockService.class));
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        for (int valueOf : manager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class))) {
            updateAppWidget(context, manager, Integer.valueOf(valueOf), 1002);
        }
    }

    private void clear(Context context) {
        context.stopService(new Intent(context, SmartClockService.class));
    }

    public void onEnabled(Context context) {
        super.onEnabled(context);
        init(context);
    }

    public void onDisabled(Context context) {
        super.onDisabled(context);
        clear(context);
    }

    private static void updateTime(RemoteViews views, Context context) {
        if (Util.getInstance(context).getValue("display_time", true)) {
            views.setViewVisibility(R.id.time, 0);
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            String timeFormat = Util.M12;
            if (Util.getInstance(context).getValue(Util.KEY_USE_24_HOUR, false)) {
                timeFormat = Util.M24;
                views.setViewVisibility(R.id.am_pm, 8);
            } else {
                views.setViewVisibility(R.id.am_pm, 0);
                views.setTextViewText(R.id.am_pm, mCalendar.get(9) == 0 ? Util.AM_STRING : Util.PM_STRING);
                setTextSize(views, R.id.am_pm, Util.getInstance(context).getValue(Util.KEY_AMPM_SIZE, 0), 15);
            }
            views.setTextViewText(R.id.timeDisplay, DateFormat.format(timeFormat, mCalendar));
            setTextSize(views, R.id.timeDisplay, Util.getInstance(context).getValue("time_size", 0), 52);
            int timeColor = Util.getInstance(context).getValue("time_color", -1);
            Log.d("nanoha", "timeColor=" + timeColor);
            views.setTextColor(R.id.timeDisplay, timeColor);
            views.setTextColor(R.id.am_pm, timeColor);
        } else {
            views.setViewVisibility(R.id.time, 8);
        }
        if (Util.getInstance(context).getValue("display_date", true)) {
            views.setViewVisibility(R.id.date, 0);
            views.setTextViewText(R.id.date, DateFormat.format(Util.getInstance(context).getValue("date_format", context.getString(R.string.full_wday_month_day_no_year)), new Date()));
            views.setTextColor(R.id.date, Util.getInstance(context).getValue("date_color", -1));
            setTextSize(views, R.id.date, Util.getInstance(context).getValue("date_size", 0), 16);
            return;
        }
        views.setViewVisibility(R.id.date, 8);
    }

    private static void setTextSize(RemoteViews views, int viewId, int value, int defaultValue) {
        float size = (float) defaultValue;
        if (value != 0) {
            size = (float) value;
        }
        views.setFloat(viewId, "setTextSize", size);
    }

    private static void updateCarrier(RemoteViews views, Context context) {
        if (Util.getInstance(context).getValue("display_carrier", true)) {
            views.setViewVisibility(R.id.carrier, 0);
            KeyguardUpdateMonitor mUpdateMonitor = KeyguardUpdateMonitor.getInstance(context.getApplicationContext());
            views.setTextViewText(R.id.carrier, getCarrierString(mUpdateMonitor.getTelephonyPlmn(), mUpdateMonitor.getTelephonySpn()));
            views.setTextColor(R.id.carrier, Util.getInstance(context).getValue("carrier_color", -1));
            setTextSize(views, R.id.carrier, Util.getInstance(context).getValue("carrier_size", 0), 16);
            return;
        }
        views.setViewVisibility(R.id.carrier, 8);
    }

    private static void updateBatteryInfo(RemoteViews views, Context context) {
        String mCharging;
        if (Util.getInstance(context).getValue(Util.KEY_DISPLAY_CHARGEINFO, true)) {
            views.setViewVisibility(R.id.status1, 0);
            KeyguardUpdateMonitor monitor = KeyguardUpdateMonitor.getInstance(context);
            boolean mPluggedIn = monitor.getPlugIn();
            int mBatteryLevel = monitor.getBatteryLevel();
            if (!mPluggedIn) {
                mCharging = context.getString(R.string.lockscreen_current_battery, new Object[]{Integer.valueOf(mBatteryLevel)});
            } else if (mBatteryLevel >= 100) {
                mCharging = context.getString(R.string.lockscreen_charged);
            } else {
                mCharging = context.getString(R.string.lockscreen_plugged_in, new Object[]{Integer.valueOf(mBatteryLevel)});
            }
            views.setTextViewText(R.id.status1, mCharging);
            views.setTextColor(R.id.status1, Util.getInstance(context).getValue(Util.KEY_CHARGEINFO_COLOR, -1));
            setTextSize(views, R.id.status1, Util.getInstance(context).getValue(Util.KEY_CHARGEINFO_SIZE, 0), 16);
            return;
        }
        views.setViewVisibility(R.id.status1, 8);
    }

    static CharSequence getCarrierString(CharSequence telephonyPlmn, CharSequence telephonySpn) {
        if (telephonyPlmn != null && telephonySpn == null) {
            return telephonyPlmn;
        }
        if (telephonyPlmn != null && telephonySpn != null) {
            return telephonyPlmn + "|" + telephonySpn;
        }
        if (telephonyPlmn != null || telephonySpn == null) {
            return "";
        }
        return telephonySpn;
    }

    private static void updateAlarmInfo(RemoteViews views, Context context) {
        if (Util.getInstance(context).getValue(Util.KEY_DISPLAY_ALARM, true)) {
            views.setViewVisibility(R.id.status2, 0);
            String mNextAlarm = getNextAlarm(context);
            if (mNextAlarm == null) {
                views.setViewVisibility(R.id.status2, 4);
                return;
            }
            views.setViewVisibility(R.id.status2, 0);
            views.setTextViewText(R.id.status2, mNextAlarm);
            views.setTextColor(R.id.status2, Util.getInstance(context).getValue(Util.KEY_ALARM_COLOR, -1));
            setTextSize(views, R.id.status2, Util.getInstance(context).getValue(Util.KEY_ALARM_SIZE, 0), 16);
            return;
        }
        views.setViewVisibility(R.id.status2, 8);
    }

    private static String getNextAlarm(Context context) {
        String nextAlarm = Settings.System.getString(context.getContentResolver(), "next_alarm_formatted");
        if (nextAlarm == null || TextUtils.isEmpty(nextAlarm)) {
            return null;
        }
        return nextAlarm;
    }

    private static void updateWidgetClick(RemoteViews views, Context context, Integer mAppWidgetId) {
        Intent widget_intent = new Intent();
        switch (Util.getInstance(context).getValue(Util.KEY_WIDGET_CLICK_ACTION, 1)) {
            case 1:
                widget_intent.setClass(context, SmartClock.class);
                break;
            case 2:
                Intent alarmIntent = getAlarmIntent(context);
                if (alarmIntent != null) {
                    widget_intent = alarmIntent;
                    break;
                }
                break;
        }
        widget_intent.putExtra("appWidgetId", mAppWidgetId);
        views.setOnClickPendingIntent(R.id.root_layout, PendingIntent.getActivity(context, 0, widget_intent, 0));
    }

    private static Intent getAlarmIntent(Context paramContext) {
        Intent alarmIntent = new Intent();
        boolean alreadyFind = false;
        switch (0) {
            case 0:
                alarmIntent.setClassName("com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
            case 1:
                alarmIntent.setClassName("com.android.deskclock", "com.android.deskclock.AlarmClock");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
            case 2:
                alarmIntent.setClassName("com.google.android.deskclock", "com.google.android.deskclock");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
            case 3:
                alarmIntent.setClassName("com.motorola.blur.alarmclock", "com.motorola.blur.alarmclock");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
            case 4:
                alarmIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
            case 5:
                alarmIntent.setClassName("com.android.alarmclock", "com.android.alarmclock.AlarmClock");
                if (isIntentAvailable(paramContext, alarmIntent)) {
                    alreadyFind = true;
                    break;
                }
                break;
        }
        if (!alreadyFind) {
            return null;
        }
        return alarmIntent;
    }

    public static boolean isIntentAvailable(Context paramContext, Intent paramIntent) {
        if (paramContext.getPackageManager().queryIntentActivities(paramIntent, 65536).size() > 0) {
            return true;
        }
        return false;
    }

    private static void updateAll(RemoteViews views, Context context, Integer mAppWidgetId) {
        updateWidgetClick(views, context, mAppWidgetId);
        updateCarrier(views, context);
        updateTime(views, context);
        updateBatteryInfo(views, context);
        updateAlarmInfo(views, context);
    }

    public static synchronized void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Integer mAppWidgetId, int from) {
        synchronized (WidgetProvider.class) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget_layout);
            if (from == 1002) {
                updateAll(views, context, mAppWidgetId);
            } else if (from == 1001) {
                updateTime(views, context);
            } else if (from == 1003) {
                updateCarrier(views, context);
            } else if (from == 1004) {
                updateBatteryInfo(views, context);
            } else if (from == 1005) {
                updateAlarmInfo(views, context);
            } else if (from == 1006) {
                updateAll(views, context, mAppWidgetId);
            }
            appWidgetManager.updateAppWidget(mAppWidgetId.intValue(), views);
        }
    }
}
