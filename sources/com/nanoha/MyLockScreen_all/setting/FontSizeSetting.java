package com.nanoha.MyLockScreen_all.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class FontSizeSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    ListPreference amSize;
    ListPreference carrierSize;
    ListPreference dateSize;
    CheckBoxPreference size_screen_adapt;
    ListPreference statusSize;
    ListPreference timeSize;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.util = Util.getInstance(this);
        addPreferencesFromResource(R.xml.fontsize_setting);
        initView();
    }

    private void initView() {
        this.size_screen_adapt = (CheckBoxPreference) findPreference(Util.KEY_SIZE_AUTO);
        this.timeSize = (ListPreference) findPreference("time_size");
        this.amSize = (ListPreference) findPreference(Util.KEY_AM_SIZE);
        this.dateSize = (ListPreference) findPreference("date_size");
        this.statusSize = (ListPreference) findPreference(Util.KEY_STATUS_SIZE);
        this.carrierSize = (ListPreference) findPreference("carrier_size");
        this.timeSize.setOnPreferenceChangeListener(this);
        this.amSize.setOnPreferenceChangeListener(this);
        this.dateSize.setOnPreferenceChangeListener(this);
        this.statusSize.setOnPreferenceChangeListener(this);
        this.carrierSize.setOnPreferenceChangeListener(this);
        this.size_screen_adapt.setChecked(this.util.isSizeAuto());
        int timeFontSize = this.util.getTimeSize();
        int amFontSize = this.util.getAmSize();
        int dateFontSize = this.util.getDateSize();
        int statusFontSize = this.util.getStatusSize();
        int carrierFontSize = this.util.getCarrierSize();
        String screenAdapt = getString(R.string.screen_adapt);
        if (timeFontSize == 0) {
            this.timeSize.setSummary(screenAdapt);
        } else {
            this.timeSize.setSummary(String.valueOf(timeFontSize) + "dip");
        }
        if (amFontSize == 0) {
            this.amSize.setSummary(screenAdapt);
        } else {
            this.amSize.setSummary(String.valueOf(amFontSize) + "dip");
        }
        if (dateFontSize == 0) {
            this.dateSize.setSummary(screenAdapt);
        } else {
            this.dateSize.setSummary(String.valueOf(dateFontSize) + "dip");
        }
        if (statusFontSize == 0) {
            this.statusSize.setSummary(screenAdapt);
        } else {
            this.statusSize.setSummary(String.valueOf(statusFontSize) + "dip");
        }
        if (carrierFontSize == 0) {
            this.carrierSize.setSummary(screenAdapt);
        } else {
            this.carrierSize.setSummary(String.valueOf(carrierFontSize) + "dip");
        }
        updateView();
    }

    private void updateView() {
        if (this.size_screen_adapt.isChecked()) {
            this.timeSize.setEnabled(false);
            this.amSize.setEnabled(false);
            this.dateSize.setEnabled(false);
            this.statusSize.setEnabled(false);
            this.carrierSize.setEnabled(false);
            return;
        }
        this.timeSize.setEnabled(true);
        this.amSize.setEnabled(true);
        this.dateSize.setEnabled(true);
        this.statusSize.setEnabled(true);
        this.carrierSize.setEnabled(true);
    }

    public boolean onPreferenceChange(Preference pref, Object val) {
        String key = pref.getKey();
        int size = Integer.parseInt((String) val);
        this.util.saveFontSize(key, size);
        if (size == 0) {
            pref.setSummary(getString(R.string.screen_adapt));
            return true;
        }
        pref.setSummary(String.valueOf(size) + "dip");
        return true;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (!key.equals(Util.KEY_SIZE_AUTO)) {
            return true;
        }
        this.util.saveIsAuto(key, this.size_screen_adapt.isChecked());
        updateView();
        return true;
    }
}
