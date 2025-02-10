package com.nanoha.MyLockScreen_all.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.EditText;
import com.nanoha.CropImage.CropImage;
import com.nanoha.MyLockScreen_all.MyLockScreen;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class DisplaySetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    static final int REQUEST_CROP_IMG = 102;
    static final int REQUEST_SELEC_BACKGROUND = 101;
    CheckBoxPreference always_display_battery;
    EditTextPreference background_alpha_setting;
    Preference background_setting;
    ListPreference custom_theme;
    CheckBoxPreference display_carrier;
    CheckBoxPreference display_date;
    CheckBoxPreference display_lock_icon;
    CheckBoxPreference display_music_control;
    CheckBoxPreference display_mute_icon;
    CheckBoxPreference display_status;
    CheckBoxPreference display_statusbar;
    CheckBoxPreference display_time;
    CheckBoxPreference only_display_lock_circle;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.display_setting);
        this.util = Util.getInstance(this);
        initView();
    }

    private void initView() {
        this.background_setting = findPreference(Util.KEY_BACKGROUND);
        this.display_lock_icon = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_LOCK_ICON);
        this.display_carrier = (CheckBoxPreference) findPreference("display_carrier");
        this.display_statusbar = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_STATUSBAR);
        this.only_display_lock_circle = (CheckBoxPreference) findPreference(Util.KEY_ONLY_DISPLAY_LOCK_CIRCLE);
        this.display_mute_icon = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_MUTE_ICON);
        this.display_music_control = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_MUSIC_CONTROL);
        this.background_alpha_setting = (EditTextPreference) findPreference(Util.KEY_BACKGROUND_ALPHA_SETTING);
        this.always_display_battery = (CheckBoxPreference) findPreference(Util.KEY_ALWAYS_DISPLAY_BATTERY);
        this.display_time = (CheckBoxPreference) findPreference("display_time");
        this.display_time.setOnPreferenceChangeListener(this);
        this.display_date = (CheckBoxPreference) findPreference("display_date");
        this.display_date.setOnPreferenceChangeListener(this);
        this.display_status = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_STATUS);
        this.display_status.setOnPreferenceChangeListener(this);
        this.custom_theme = (ListPreference) findPreference(Util.KEY_THEME);
        this.custom_theme.setOnPreferenceChangeListener(this);
        this.always_display_battery.setOnPreferenceChangeListener(this);
        this.background_alpha_setting.setOnPreferenceChangeListener(this);
        this.display_music_control.setOnPreferenceChangeListener(this);
        this.display_mute_icon.setOnPreferenceChangeListener(this);
        this.only_display_lock_circle.setOnPreferenceChangeListener(this);
        this.display_lock_icon.setOnPreferenceChangeListener(this);
        this.background_setting.setOnPreferenceChangeListener(this);
        this.display_carrier.setOnPreferenceChangeListener(this);
        this.display_statusbar.setOnPreferenceChangeListener(this);
        if (this.util.isDisplayCarrier()) {
            this.display_carrier.setChecked(true);
        } else {
            this.display_carrier.setChecked(false);
        }
        if (this.util.isDisplayStatusbar()) {
            this.display_statusbar.setChecked(true);
        } else {
            this.display_statusbar.setChecked(false);
        }
        if (this.util.isDisplayLockIcon()) {
            this.display_lock_icon.setChecked(true);
        } else {
            this.display_lock_icon.setChecked(false);
        }
        if (this.util.isDisplayMuteIcon()) {
            this.display_mute_icon.setChecked(true);
        } else {
            this.display_mute_icon.setChecked(false);
        }
        if (this.util.isDisplayMusicControl()) {
            this.display_music_control.setChecked(true);
        } else {
            this.display_music_control.setChecked(false);
        }
        if (this.util.isOnlyDisplayLockCircle()) {
            this.only_display_lock_circle.setChecked(true);
            this.display_time.setEnabled(false);
            this.display_date.setEnabled(false);
            this.display_status.setEnabled(false);
            this.display_carrier.setEnabled(false);
        } else {
            this.only_display_lock_circle.setChecked(false);
            this.display_time.setEnabled(true);
            this.display_date.setEnabled(true);
            this.display_status.setEnabled(true);
            this.display_carrier.setEnabled(true);
        }
        if (this.util.isDisplayBattery()) {
            this.always_display_battery.setChecked(true);
        } else {
            this.always_display_battery.setChecked(false);
        }
        if (this.util.isDisplayTime()) {
            this.display_time.setChecked(true);
        } else {
            this.display_time.setChecked(false);
        }
        if (this.util.isDisplayDate()) {
            this.display_date.setChecked(true);
        } else {
            this.display_date.setChecked(false);
        }
        if (this.util.isDisplayStatus()) {
            this.display_status.setChecked(true);
        } else {
            this.display_status.setChecked(false);
        }
        EditText alphaText = this.background_alpha_setting.getEditText();
        alphaText.setText(String.valueOf(this.util.getBackgroundAlpha()));
        alphaText.setInputType(2);
        if (MyLockScreen.hasAd) {
            this.background_setting.setEnabled(false);
            this.background_alpha_setting.setEnabled(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == -1) {
            Uri uri = data.getData();
            Bundle newExtras = new Bundle();
            Intent cropIntent = new Intent();
            cropIntent.setData(uri);
            cropIntent.setClass(this, CropImage.class);
            cropIntent.putExtras(newExtras);
            startActivityForResult(cropIntent, 102);
        } else if (requestCode == 102 && resultCode == -1) {
            this.util.saveBackground(data.getData().toString());
        }
    }

    public boolean onPreferenceChange(Preference pref, Object objVal) {
        boolean onlyDisplay;
        String key = pref.getKey();
        if (key.equals(Util.KEY_BACKGROUND)) {
            if ("0".equals(objVal)) {
                this.util.saveBackground("0");
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(intent, 101);
            }
        } else if (key.equals("display_carrier")) {
            this.util.saveDisplayCarrier(!this.display_carrier.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_STATUSBAR)) {
            this.util.saveDisplayStatusbar(!this.display_statusbar.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_LOCK_ICON)) {
            this.util.saveDisplayLockIcon(!this.display_lock_icon.isChecked());
        } else if (key.equals(Util.KEY_ALWAYS_DISPLAY_BATTERY)) {
            this.util.saveDisplayBattery(!this.always_display_battery.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_MUTE_ICON)) {
            this.util.saveDisplayMuteIcon(!this.display_mute_icon.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_MUSIC_CONTROL)) {
            this.util.saveDisplayMusicControl(!this.display_music_control.isChecked());
        } else if (key.equals(Util.KEY_ONLY_DISPLAY_LOCK_CIRCLE)) {
            if (this.only_display_lock_circle.isChecked()) {
                onlyDisplay = false;
            } else {
                onlyDisplay = true;
            }
            this.util.saveOnlyDisplayLockCircle(onlyDisplay);
            if (onlyDisplay) {
                this.display_time.setEnabled(false);
                this.display_date.setEnabled(false);
                this.display_status.setEnabled(false);
                this.display_carrier.setEnabled(false);
            } else {
                this.display_time.setEnabled(true);
                this.display_date.setEnabled(true);
                this.display_status.setEnabled(true);
                this.display_carrier.setEnabled(true);
            }
        } else if (key.equals(Util.KEY_BACKGROUND_ALPHA_SETTING)) {
            if (objVal == null || "".equals(objVal)) {
                objVal = "0";
            }
            int alpha = Integer.parseInt(String.valueOf(objVal));
            if (alpha > 200) {
                alpha = 200;
            }
            this.util.saveBackgroundAlpha(alpha);
        } else if (key.equals("display_time")) {
            this.util.saveDisplayTime(!this.display_time.isChecked());
        } else if (key.equals("display_date")) {
            this.util.saveDisplayDate(!this.display_date.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_STATUS)) {
            this.util.saveDisplayStatus(!this.display_status.isChecked());
        } else if (key.equals(Util.KEY_THEME)) {
            this.util.saveValue(Util.KEY_THEME, Integer.parseInt((String) objVal));
        }
        return true;
    }
}
