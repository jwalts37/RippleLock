package com.nanoha.smartClock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.nanoha.MyLockScreen_all.R;

public class SmartClock extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    ListPreference alarm_size;
    ListPreference ampm_size;
    ListPreference carrier_size;
    ListPreference chargeinfo_size;
    ListPreference date_format;
    ListPreference date_size;
    CheckBoxPreference display_alarm;
    CheckBoxPreference display_carrier;
    CheckBoxPreference display_chargeinfo;
    CheckBoxPreference display_date;
    CheckBoxPreference display_time;
    Preference how_to_use;
    ListPreference time_size;
    CheckBoxPreference use_24_hour;
    Util util;
    ListPreference widget_click_action;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.clock_main_setting);
        setContentView(R.layout.clock_setting);
        this.util = Util.getInstance(this);
        initView();
        startService(new Intent(this, SmartClockService.class));
    }

    private void initView() {
        this.how_to_use = findPreference("how_to_use");
        this.display_carrier = (CheckBoxPreference) findPreference("display_carrier");
        this.display_carrier.setOnPreferenceChangeListener(this);
        this.display_time = (CheckBoxPreference) findPreference("display_time");
        this.display_time.setOnPreferenceChangeListener(this);
        this.display_date = (CheckBoxPreference) findPreference("display_date");
        this.display_date.setOnPreferenceChangeListener(this);
        this.display_chargeinfo = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_CHARGEINFO);
        this.display_chargeinfo.setOnPreferenceChangeListener(this);
        this.display_alarm = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_ALARM);
        this.display_alarm.setOnPreferenceChangeListener(this);
        this.widget_click_action = (ListPreference) findPreference(Util.KEY_WIDGET_CLICK_ACTION);
        this.widget_click_action.setOnPreferenceChangeListener(this);
        this.date_format = (ListPreference) findPreference("date_format");
        this.date_format.setOnPreferenceChangeListener(this);
        this.use_24_hour = (CheckBoxPreference) findPreference(Util.KEY_USE_24_HOUR);
        this.use_24_hour.setOnPreferenceChangeListener(this);
        this.carrier_size = (ListPreference) findPreference("carrier_size");
        this.carrier_size.setOnPreferenceChangeListener(this);
        this.time_size = (ListPreference) findPreference("time_size");
        this.time_size.setOnPreferenceChangeListener(this);
        this.ampm_size = (ListPreference) findPreference(Util.KEY_AMPM_SIZE);
        this.ampm_size.setOnPreferenceChangeListener(this);
        this.date_size = (ListPreference) findPreference("date_size");
        this.date_size.setOnPreferenceChangeListener(this);
        this.chargeinfo_size = (ListPreference) findPreference(Util.KEY_CHARGEINFO_SIZE);
        this.chargeinfo_size.setOnPreferenceChangeListener(this);
        this.alarm_size = (ListPreference) findPreference(Util.KEY_ALARM_SIZE);
        this.alarm_size.setOnPreferenceChangeListener(this);
        if (this.util.getValue("display_carrier", true)) {
            this.display_carrier.setChecked(true);
        } else {
            this.display_carrier.setChecked(false);
        }
        if (this.util.getValue("display_time", true)) {
            this.display_time.setChecked(true);
        } else {
            this.display_time.setChecked(false);
        }
        if (this.util.getValue("display_date", true)) {
            this.display_date.setChecked(true);
        } else {
            this.display_date.setChecked(false);
        }
        if (this.util.getValue(Util.KEY_DISPLAY_CHARGEINFO, true)) {
            this.display_chargeinfo.setChecked(true);
        } else {
            this.display_chargeinfo.setChecked(false);
        }
        if (this.util.getValue(Util.KEY_DISPLAY_ALARM, true)) {
            this.display_alarm.setChecked(true);
        } else {
            this.display_alarm.setChecked(false);
        }
        if (this.util.getValue(Util.KEY_USE_24_HOUR, false)) {
            this.use_24_hour.setChecked(true);
        } else {
            this.use_24_hour.setChecked(false);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference != this.how_to_use) {
            return false;
        }
        new AlertDialog.Builder(this).setTitle(R.string.how_to_use).setMessage(R.string.how_to_use_desc).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
        return false;
    }

    public boolean onPreferenceChange(Preference pref, Object objVal) {
        boolean z;
        String key = pref.getKey();
        if (key.equals("display_carrier")) {
            Util util2 = this.util;
            if (this.display_carrier.isChecked()) {
                z = false;
            } else {
                z = true;
            }
            util2.saveValue("display_carrier", z);
        } else if (key.equals("display_time")) {
            this.util.saveValue("display_time", !this.display_time.isChecked());
        } else if (key.equals("display_date")) {
            this.util.saveValue("display_date", !this.display_date.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_CHARGEINFO)) {
            this.util.saveValue(Util.KEY_DISPLAY_CHARGEINFO, !this.display_chargeinfo.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_ALARM)) {
            this.util.saveValue(Util.KEY_DISPLAY_ALARM, !this.display_alarm.isChecked());
        } else if (key.equals(Util.KEY_USE_24_HOUR)) {
            this.util.saveValue(Util.KEY_USE_24_HOUR, !this.use_24_hour.isChecked());
        } else if (key.equals(Util.KEY_WIDGET_CLICK_ACTION)) {
            this.util.saveValue(Util.KEY_WIDGET_CLICK_ACTION, Integer.parseInt(String.valueOf(objVal)));
        } else if (key.equals("date_format")) {
            this.util.saveValue("date_format", String.valueOf(objVal));
        } else if (key.equals("carrier_size") || key.equals("time_size") || key.equals(Util.KEY_AMPM_SIZE) || key.equals("date_size") || key.equals(Util.KEY_CHARGEINFO_SIZE) || key.equals(Util.KEY_ALARM_SIZE)) {
            this.util.saveValue(key, Integer.parseInt(String.valueOf(objVal)));
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        sendBroadcast(new Intent(Util.INTENT_ACTION_SETTING_CHANGE));
    }
}
