package com.nanoha.MyLockScreen_all.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.nanoha.MyLockScreen_all.R;

public class RippleLockSetting extends PreferenceActivity {
    Preference display_setting;
    Preference font_setting;
    Preference other_setting;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ripplelock_setting);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (key.equals("font_setting")) {
            Intent fontSettingIntent = new Intent();
            fontSettingIntent.setClass(this, FontSetting.class);
            startActivity(fontSettingIntent);
            return false;
        } else if (key.equals("display_setting")) {
            Intent displaySetting = new Intent();
            displaySetting.setClass(this, DisplaySetting.class);
            startActivity(displaySetting);
            return false;
        } else if (!key.equals("other_setting")) {
            return false;
        } else {
            Intent otherSetting = new Intent();
            otherSetting.setClass(this, OtherSetting.class);
            startActivity(otherSetting);
            return false;
        }
    }
}
