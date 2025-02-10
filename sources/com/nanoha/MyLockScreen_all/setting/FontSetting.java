package com.nanoha.MyLockScreen_all.setting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.MyLockScreen;
import com.nanoha.MyLockScreen_all.MyLockService;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class FontSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    static final int REQUEST_SELECT_FONT = 103;
    Preference font_color;
    Preference font_size;
    ListPreference font_style;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.util = Util.getInstance(this);
        addPreferencesFromResource(R.xml.font_setting);
        initView();
    }

    private void initView() {
        this.font_size = findPreference("font_size");
        this.font_color = findPreference("font_color");
        this.font_style = (ListPreference) findPreference(Util.KEY_FONT_STYLE);
        this.font_style.setOnPreferenceChangeListener(this);
        if (MyLockScreen.hasAd) {
            this.font_style.setEnabled(false);
            this.font_color.setEnabled(false);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.font_size) {
            Intent fontSizeIntent = new Intent();
            fontSizeIntent.setClass(this, FontSizeSetting.class);
            startActivity(fontSizeIntent);
            return false;
        } else if (preference != this.font_color) {
            return false;
        } else {
            Intent fontColorIntent = new Intent();
            fontColorIntent.setClass(this, FontColorSetting.class);
            startActivity(fontColorIntent);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 103 && resultCode == -1) {
            String path = data.getData().toString().replace("file://", "");
            try {
                Typeface.createFromFile(path);
                this.util.saveFontStyle(5);
                this.util.saveCustomFontPath(path);
            } catch (Exception e) {
                Toast.makeText(this, R.string.not_support_font, 0).show();
            }
        }
    }

    public void resetService() {
        if (getSharedPreferences(MyLockScreen.MYLOCKSCREEN_PREF, 2).getInt(MyLockScreen.PREF_STATE, 2) == 1) {
            Intent service = new Intent(this, MyLockService.class);
            stopService(service);
            startService(service);
        }
    }

    public boolean onPreferenceChange(Preference pref, Object objVal) {
        if (!pref.getKey().equals(Util.KEY_FONT_STYLE)) {
            return true;
        }
        int intVal = Integer.parseInt(String.valueOf(objVal));
        if (intVal == 5) {
            Intent intent = new Intent();
            intent.setType("application/octet-stream");
            intent.setAction("android.intent.action.GET_CONTENT");
            try {
                startActivityForResult(intent, 103);
                return true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.install_filemanager, 0).show();
                return true;
            }
        } else {
            this.util.saveFontStyle(intVal);
            return true;
        }
    }
}
