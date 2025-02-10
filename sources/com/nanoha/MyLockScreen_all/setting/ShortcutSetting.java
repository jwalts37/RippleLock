package com.nanoha.MyLockScreen_all.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class ShortcutSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static final int REQUEST_CROP_IMG = 102;
    public static final int REQUEST_SELECT_SHORTCUT_ICON = 1003;
    static final int REQUEST_SELEC_SHORTCUT_ICON = 101;
    public static final int SELECT_SHORTCUT_APP = 1002;
    public static final String SELECT_WHICH_SHORTCUT = "which_shortcut";
    public static final String SHORTCUT_ICON = "shortcut_icon";
    ListPreference bottom_shortcut_setting;
    ListPreference bottom_shortcut_setting_icon;
    CheckBoxPreference display_quickview;
    ListPreference left_shortcut_setting;
    ListPreference left_shortcut_setting_icon;
    ProgressDialog progressDialog;
    ListPreference right_shortcut_setting;
    ListPreference right_shortcut_setting_icon;
    String selectedKey = null;
    ListPreference top_shortcut_setting;
    ListPreference top_shortcut_setting_icon;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.util = Util.getInstance(this);
        addPreferencesFromResource(R.xml.shortcut_setting);
        initView();
    }

    private void initView() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage(getString(R.string.loading));
        this.display_quickview = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_QUICKVIEW);
        this.left_shortcut_setting = (ListPreference) findPreference(Util.KEY_LEFT_SHORTCUT_SETTING);
        this.top_shortcut_setting = (ListPreference) findPreference(Util.KEY_TOP_SHORTCUT_SETTING);
        this.right_shortcut_setting = (ListPreference) findPreference(Util.KEY_RIGHT_SHORTCUT_SETTING);
        this.bottom_shortcut_setting = (ListPreference) findPreference(Util.KEY_BOTTOM_SHORTCUT_SETTING);
        this.left_shortcut_setting_icon = (ListPreference) findPreference("left_shortcut_setting_icon");
        this.top_shortcut_setting_icon = (ListPreference) findPreference("top_shortcut_setting_icon");
        this.right_shortcut_setting_icon = (ListPreference) findPreference("right_shortcut_setting_icon");
        this.bottom_shortcut_setting_icon = (ListPreference) findPreference("bottom_shortcut_setting_icon");
        this.left_shortcut_setting.setOnPreferenceChangeListener(this);
        this.top_shortcut_setting.setOnPreferenceChangeListener(this);
        this.right_shortcut_setting.setOnPreferenceChangeListener(this);
        this.bottom_shortcut_setting.setOnPreferenceChangeListener(this);
        this.left_shortcut_setting_icon.setOnPreferenceChangeListener(this);
        this.top_shortcut_setting_icon.setOnPreferenceChangeListener(this);
        this.right_shortcut_setting_icon.setOnPreferenceChangeListener(this);
        this.bottom_shortcut_setting_icon.setOnPreferenceChangeListener(this);
        this.display_quickview.setOnPreferenceChangeListener(this);
        if (this.util.isDisplayQuickView()) {
            this.display_quickview.setChecked(true);
            this.left_shortcut_setting.setEnabled(true);
            this.right_shortcut_setting.setEnabled(true);
            this.top_shortcut_setting.setEnabled(true);
            this.bottom_shortcut_setting.setEnabled(true);
            this.left_shortcut_setting_icon.setEnabled(true);
            this.right_shortcut_setting_icon.setEnabled(true);
            this.top_shortcut_setting_icon.setEnabled(true);
            this.bottom_shortcut_setting_icon.setEnabled(true);
            return;
        }
        this.display_quickview.setChecked(false);
        this.left_shortcut_setting.setEnabled(false);
        this.right_shortcut_setting.setEnabled(false);
        this.top_shortcut_setting.setEnabled(false);
        this.bottom_shortcut_setting.setEnabled(false);
        this.display_quickview.setChecked(false);
        this.left_shortcut_setting_icon.setEnabled(false);
        this.right_shortcut_setting_icon.setEnabled(false);
        this.top_shortcut_setting_icon.setEnabled(false);
        this.bottom_shortcut_setting_icon.setEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String key;
        this.progressDialog.dismiss();
        if (requestCode == 1002 && resultCode == -1) {
            String key2 = data.getStringExtra(SELECT_WHICH_SHORTCUT);
            if (key2 != null) {
                this.util.saveShortcutSetting(key2, data.toURI());
            }
        } else if (requestCode == 101 && resultCode == -1 && (key = data.getStringExtra(SELECT_WHICH_SHORTCUT)) != null) {
            this.util.saveShortcutIcon(key, data.getStringExtra(SHORTCUT_ICON));
        }
    }

    public boolean onPreferenceChange(Preference pref, Object objVal) {
        String key = pref.getKey();
        if (key.equals(Util.KEY_DISPLAY_QUICKVIEW)) {
            boolean display = !this.display_quickview.isChecked();
            this.util.saveDisplayQuickView(display);
            if (display) {
                this.left_shortcut_setting.setEnabled(true);
                this.right_shortcut_setting.setEnabled(true);
                this.top_shortcut_setting.setEnabled(true);
                this.bottom_shortcut_setting.setEnabled(true);
                this.left_shortcut_setting_icon.setEnabled(true);
                this.right_shortcut_setting_icon.setEnabled(true);
                this.top_shortcut_setting_icon.setEnabled(true);
                this.bottom_shortcut_setting_icon.setEnabled(true);
            } else {
                this.left_shortcut_setting.setEnabled(false);
                this.right_shortcut_setting.setEnabled(false);
                this.top_shortcut_setting.setEnabled(false);
                this.bottom_shortcut_setting.setEnabled(false);
                this.left_shortcut_setting_icon.setEnabled(false);
                this.right_shortcut_setting_icon.setEnabled(false);
                this.top_shortcut_setting_icon.setEnabled(false);
                this.bottom_shortcut_setting_icon.setEnabled(false);
            }
        } else if (!key.contains(Util.KEY_SHORTCU_ICON)) {
            if ("0".equals(objVal)) {
                this.util.saveShortcutSetting(key, "0");
            } else if ("1".equals(objVal)) {
                this.progressDialog.show();
                Intent intent = new Intent(this, SelectAppActivity.class);
                intent.putExtra(SELECT_WHICH_SHORTCUT, key);
                startActivityForResult(intent, 1002);
            }
        } else if ("0".equals(objVal)) {
            this.util.saveShortcutIcon(key, "-1");
        } else if ("1".equals(objVal)) {
            Intent selectIconintent = new Intent(this, SelectShortcutIcon.class);
            selectIconintent.putExtra(SELECT_WHICH_SHORTCUT, key);
            startActivityForResult(selectIconintent, 101);
        }
        return true;
    }
}
