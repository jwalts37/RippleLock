package com.nanoha.MyLockScreen_all.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class FontColorSetting extends PreferenceActivity {
    ColorPickerPreference am_color;
    ColorPickerPreference carrier_color;
    ColorPickerPreference date_color;
    CheckBoxPreference default_color;
    ColorPickerPreference status_color;
    ColorPickerPreference time_color;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fontcolor_setting);
        this.util = Util.getInstance(this);
        initView();
    }

    private void initView() {
        this.default_color = (CheckBoxPreference) findPreference(Util.KEY_COLOR_AUTO);
        this.time_color = (ColorPickerPreference) findPreference("time_color");
        this.am_color = (ColorPickerPreference) findPreference(Util.KEY_AM_COLOR);
        this.date_color = (ColorPickerPreference) findPreference("date_color");
        this.status_color = (ColorPickerPreference) findPreference(Util.KEY_STATUS_COLOR);
        this.carrier_color = (ColorPickerPreference) findPreference("carrier_color");
        this.default_color.setChecked(this.util.isColorAuto());
        updateView();
    }

    private void updateView() {
        if (this.default_color.isChecked()) {
            this.time_color.setEnabled(false);
            this.am_color.setEnabled(false);
            this.date_color.setEnabled(false);
            this.status_color.setEnabled(false);
            this.carrier_color.setEnabled(false);
            return;
        }
        this.time_color.setEnabled(true);
        this.am_color.setEnabled(true);
        this.date_color.setEnabled(true);
        this.status_color.setEnabled(true);
        this.carrier_color.setEnabled(true);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (!key.equals(Util.KEY_COLOR_AUTO)) {
            return true;
        }
        this.util.saveIsAuto(key, this.default_color.isChecked());
        updateView();
        return true;
    }
}
