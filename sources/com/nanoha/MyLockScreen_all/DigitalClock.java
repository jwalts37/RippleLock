package com.nanoha.MyLockScreen_all;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nanoha.util.Util;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DigitalClock extends RelativeLayout {
    private static final String M12 = "h:mm";
    private static final String M24 = "kk:mm";
    private AmPm mAmPm;
    private boolean mAttached;
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    /* access modifiers changed from: private */
    public Context mContext;
    private String mFormat;
    private ContentObserver mFormatChangeObserver;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    /* access modifiers changed from: private */
    public boolean mLive;
    private TextView mTimeDisplay;

    class AmPm {
        private TextView mAmPm;
        private String mAmString;
        private String mPmString;
        Util util;

        AmPm(View parent, Typeface tf) {
            this.util = Util.getInstance(DigitalClock.this.mContext);
            this.mAmPm = (TextView) parent.findViewById(R.id.am_pm);
            if (tf != null) {
                this.mAmPm.setTypeface(tf);
            }
            String[] ampm = new DateFormatSymbols().getAmPmStrings();
            this.mAmString = ampm[0];
            this.mPmString = ampm[1];
        }

        /* access modifiers changed from: package-private */
        public void setShowAmPm(boolean show) {
            int i = 8;
            if (this.util.isOnlyDisplayLockCircle() || !this.util.isDisplayTime()) {
                this.mAmPm.setVisibility(8);
                return;
            }
            TextView textView = this.mAmPm;
            if (show) {
                i = 0;
            }
            textView.setVisibility(i);
        }

        /* access modifiers changed from: package-private */
        public void setIsMorning(boolean isMorning) {
            this.mAmPm.setText(isMorning ? this.mAmString : this.mPmString);
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            DigitalClock.this.setDateFormat();
            DigitalClock.this.updateTime();
        }
    }

    public DigitalClock(Context context) {
        this(context, (AttributeSet) null);
        this.mContext = context;
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLive = true;
        this.mHandler = new Handler();
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (DigitalClock.this.mLive && intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")) {
                    DigitalClock.this.mCalendar = Calendar.getInstance();
                }
                DigitalClock.this.mHandler.post(new Runnable() {
                    public void run() {
                        DigitalClock.this.updateTime();
                    }
                });
            }
        };
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
        this.mTimeDisplay.setTypeface(Typeface.createFromFile("/system/fonts/Clockopia.ttf"));
        this.mAmPm = new AmPm(this, Typeface.createFromFile("/system/fonts/DroidSans-Bold.ttf"));
        this.mCalendar = Calendar.getInstance();
        setDateFormat();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            if (this.mLive) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.TIME_TICK");
                filter.addAction("android.intent.action.TIME_SET");
                filter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mContext.registerReceiver(this.mIntentReceiver, filter);
            }
            this.mFormatChangeObserver = new FormatChangeObserver();
            this.mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.mFormatChangeObserver);
            updateTime();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            this.mAttached = false;
            if (this.mLive) {
                this.mContext.unregisterReceiver(this.mIntentReceiver);
            }
            this.mContext.getContentResolver().unregisterContentObserver(this.mFormatChangeObserver);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTime(Calendar c) {
        this.mCalendar = c;
        updateTime();
    }

    /* access modifiers changed from: private */
    public void updateTime() {
        if (this.mLive) {
            this.mCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        this.mTimeDisplay.setText(DateFormat.format(this.mFormat, this.mCalendar));
        this.mAmPm.setIsMorning(this.mCalendar.get(9) == 0);
    }

    /* access modifiers changed from: private */
    public void setDateFormat() {
        this.mFormat = DateFormat.is24HourFormat(getContext()) ? "kk:mm" : "h:mm";
        this.mAmPm.setShowAmPm(this.mFormat.equals("h:mm"));
    }

    /* access modifiers changed from: package-private */
    public void setLive(boolean live) {
        this.mLive = live;
    }
}
