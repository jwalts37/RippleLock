package com.nanoha.MyLockScreen_all.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.nanoha.HomeWidzard.HomeWizard;
import com.nanoha.MyLockScreen_all.MyLockScreen;
import com.nanoha.MyLockScreen_all.MyLockService;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.MyLockScreen_all.SelectWidgetActivity;
import com.nanoha.util.Util;

public class OtherSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    Preference add_widget;
    Preference block_home_key_setting;
    CheckBoxPreference bootup_auto_execute;
    ListPreference custom_timeout;
    ListPreference date_format;
    ListPreference delay_lock;
    CheckBoxPreference disable_statusbar;
    CheckBoxPreference enable_widget_click;
    ListPreference fix_double_lock_issue;
    CheckBoxPreference lock_sound;
    ListPreference music_control;
    Preference password_setting;
    ListPreference screen_orientation;
    Preference shortcut_setting;
    ListPreference unlock_anim;
    CheckBoxPreference unlock_vibrate;
    CheckBoxPreference use_call_button;
    CheckBoxPreference use_password;
    CheckBoxPreference use_system_security_lock;
    Util util;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.other_setting);
        this.util = Util.getInstance(this);
        initView();
    }

    private void initView() {
        this.shortcut_setting = findPreference(Util.KEY_SHORTCUT_SETTING);
        this.password_setting = findPreference("password_setting");
        this.add_widget = findPreference("add_widget");
        this.use_system_security_lock = (CheckBoxPreference) findPreference(Util.KEY_USE_SYSTEM_SECURITY_LOCK);
        this.screen_orientation = (ListPreference) findPreference(Util.KEY_SCREEN_ORIENTATION);
        this.disable_statusbar = (CheckBoxPreference) findPreference(Util.KEY_DISABLE_STATUSBAR);
        this.unlock_anim = (ListPreference) findPreference(Util.KEY_UNLOCK_ANIM);
        this.date_format = (ListPreference) findPreference("date_format");
        this.fix_double_lock_issue = (ListPreference) findPreference(Util.KEY_FIX_DOUBLE_LOCK_ISSUE);
        this.unlock_vibrate = (CheckBoxPreference) findPreference(Util.KEY_UNLOCK_VIBRATE);
        this.use_password = (CheckBoxPreference) findPreference(Util.KEY_USE_PASSWORD);
        this.bootup_auto_execute = (CheckBoxPreference) findPreference(Util.KEY_BOOTUP_AUTO_EXECUTE);
        this.music_control = (ListPreference) findPreference(Util.KEY_MUSIC_CONTROL);
        this.use_call_button = (CheckBoxPreference) findPreference(Util.KEY_USE_CALL_BUTTON);
        this.enable_widget_click = (CheckBoxPreference) findPreference(Util.KEY_ENABLE_WIDGET_CLICK);
        this.lock_sound = (CheckBoxPreference) findPreference(Util.KEY_LOCK_SOUND);
        this.custom_timeout = (ListPreference) findPreference(Util.KEY_CUSTOM_TIMEOUT);
        this.delay_lock = (ListPreference) findPreference(Util.KEY_DELAY_LOCK);
        this.delay_lock.setOnPreferenceChangeListener(this);
        this.custom_timeout.setOnPreferenceChangeListener(this);
        this.lock_sound.setOnPreferenceChangeListener(this);
        this.enable_widget_click.setOnPreferenceChangeListener(this);
        this.block_home_key_setting = findPreference(Util.KEY_BLOCK_HOME_SETTING);
        this.use_call_button.setOnPreferenceChangeListener(this);
        this.use_system_security_lock.setOnPreferenceChangeListener(this);
        this.music_control.setOnPreferenceChangeListener(this);
        this.bootup_auto_execute.setOnPreferenceChangeListener(this);
        this.use_password.setOnPreferenceChangeListener(this);
        this.unlock_vibrate.setOnPreferenceChangeListener(this);
        this.screen_orientation.setOnPreferenceChangeListener(this);
        this.unlock_anim.setOnPreferenceChangeListener(this);
        this.disable_statusbar.setOnPreferenceChangeListener(this);
        this.date_format.setOnPreferenceChangeListener(this);
        this.fix_double_lock_issue.setOnPreferenceChangeListener(this);
        if (this.util.isDisableStatusbar()) {
            this.disable_statusbar.setChecked(true);
        } else {
            this.disable_statusbar.setChecked(false);
        }
        if (this.util.isUnlockVibrate()) {
            this.unlock_vibrate.setChecked(true);
        } else {
            this.unlock_vibrate.setChecked(false);
        }
        if (this.util.isUsePassword()) {
            this.use_password.setChecked(true);
        } else {
            this.use_password.setChecked(false);
            this.password_setting.setEnabled(false);
        }
        if (this.util.isBootupAutoExecute()) {
            this.bootup_auto_execute.setChecked(true);
        } else {
            this.bootup_auto_execute.setChecked(false);
        }
        if (this.util.isUseSecurityLock()) {
            this.use_system_security_lock.setChecked(true);
            this.fix_double_lock_issue.setEnabled(false);
        } else {
            this.use_system_security_lock.setChecked(false);
            this.fix_double_lock_issue.setEnabled(true);
        }
        if (this.util.isUseCallButton()) {
            this.use_call_button.setChecked(true);
        } else {
            this.use_call_button.setChecked(false);
        }
        if (this.util.isEnableWidgetClick()) {
            this.enable_widget_click.setChecked(true);
        } else {
            this.enable_widget_click.setChecked(false);
        }
        if (this.util.isPlayLockSound()) {
            this.lock_sound.setChecked(true);
        } else {
            this.lock_sound.setChecked(false);
        }
        if (MyLockScreen.hasAd) {
            this.add_widget.setEnabled(false);
            this.enable_widget_click.setEnabled(false);
            this.unlock_anim.setEnabled(false);
            this.date_format.setEnabled(false);
            this.unlock_vibrate.setEnabled(false);
            this.shortcut_setting.setEnabled(false);
            this.lock_sound.setEnabled(false);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.shortcut_setting) {
            Intent shortcutIntent = new Intent();
            shortcutIntent.setClass(this, ShortcutSetting.class);
            startActivity(shortcutIntent);
            return false;
        } else if (preference == this.add_widget) {
            new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon).setMessage(R.string.add_widget_hint).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent selectWidgetIntent = new Intent();
                    selectWidgetIntent.setClass(OtherSetting.this, SelectWidgetActivity.class);
                    OtherSetting.this.startActivity(selectWidgetIntent);
                }
            }).show();
            return false;
        } else if (preference == this.password_setting) {
            LayoutInflater inflater = LayoutInflater.from(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.password_setting_title);
            final View passwordSettingView = inflater.inflate(R.layout.input_password, (ViewGroup) null);
            final String savedPassword = this.util.getPassword();
            if (savedPassword.equals("")) {
                passwordSettingView.findViewById(R.id.ori_password_layout).setVisibility(8);
            }
            builder.setView(passwordSettingView);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String oriPassword = ((EditText) passwordSettingView.findViewById(R.id.ori_password)).getText().toString();
                    if (savedPassword.equals("") || oriPassword.equals(savedPassword)) {
                        String newPassword = ((EditText) passwordSettingView.findViewById(R.id.new_password)).getText().toString();
                        if (newPassword.equals(((EditText) passwordSettingView.findViewById(R.id.confirm_password)).getText().toString())) {
                            OtherSetting.this.util.savePassword(newPassword);
                            Util.toastMessage(OtherSetting.this, R.string.password_set_success);
                            return;
                        }
                        Util.toastMessage(OtherSetting.this, R.string.two_password_not);
                        return;
                    }
                    Util.toastMessage(OtherSetting.this, R.string.ori_password_error);
                }
            });
            builder.setNegativeButton(getString(R.string.no), (DialogInterface.OnClickListener) null);
            builder.create();
            builder.show();
            return false;
        } else if (preference != this.block_home_key_setting) {
            return false;
        } else {
            Intent homeWidzardIntent = new Intent();
            homeWidzardIntent.setClass(this, HomeWizard.class);
            startActivity(homeWidzardIntent);
            return false;
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
        boolean isUse;
        boolean isUse2;
        String key = pref.getKey();
        if (key.equals(Util.KEY_SCREEN_ORIENTATION)) {
            this.util.saveScreenOrientation(String.valueOf(objVal));
        } else if (key.equals(Util.KEY_UNLOCK_ANIM)) {
            this.util.saveUnlockAnim(Integer.valueOf(Integer.parseInt(String.valueOf(objVal))));
        } else if (key.equals(Util.KEY_MUSIC_CONTROL)) {
            int controlInt = Integer.parseInt(String.valueOf(objVal));
            if (controlInt == 0) {
                this.util.saveMusicControl(Util.MUSIC_CONTROL_DEFAULT);
            } else if (controlInt == 1) {
                this.util.saveMusicControl(Util.MUSIC_CONTROL_POWERAMP);
            } else if (controlInt == 2) {
                this.util.saveMusicControl(Util.MUSIC_CONTROL_PLAYERPRO);
            } else if (controlInt == 3) {
                this.util.saveMusicControl(Util.MUSIC_CONTROL_PLAYERPRO_TRIAL);
            }
        } else if (key.equals(Util.KEY_DISABLE_STATUSBAR)) {
            this.util.saveDisableStatusbar(!this.disable_statusbar.isChecked());
        } else if (key.equals(Util.KEY_BOOTUP_AUTO_EXECUTE)) {
            this.util.saveBootupAutoExecute(!this.bootup_auto_execute.isChecked());
        } else if (key.equals(Util.KEY_UNLOCK_VIBRATE)) {
            this.util.saveUnlockVibrate(!this.unlock_vibrate.isChecked());
        } else if (key.equals("date_format")) {
            this.util.saveDateFormat(String.valueOf(objVal));
        } else if (key.equals(Util.KEY_USE_SYSTEM_SECURITY_LOCK)) {
            resetService();
            if (this.use_system_security_lock.isChecked()) {
                isUse2 = false;
            } else {
                isUse2 = true;
            }
            this.util.saveUseSecurityLock(isUse2);
            if (isUse2) {
                this.fix_double_lock_issue.setEnabled(false);
            } else {
                this.fix_double_lock_issue.setEnabled(true);
            }
        } else if (key.equals(Util.KEY_FIX_DOUBLE_LOCK_ISSUE)) {
            int fixInt = Integer.parseInt(String.valueOf(objVal));
            resetService();
            this.util.saveFixDoubleLockIssue(Integer.valueOf(fixInt));
        } else if (key.equals(Util.KEY_USE_PASSWORD)) {
            if (this.use_password.isChecked()) {
                isUse = false;
            } else {
                isUse = true;
            }
            this.util.saveUsePassword(isUse);
            if (isUse) {
                this.password_setting.setEnabled(true);
            } else {
                this.password_setting.setEnabled(false);
            }
        } else if (key.equals(Util.KEY_USE_CALL_BUTTON)) {
            this.util.saveUseCallButton(!this.use_call_button.isChecked());
        } else if (key.equals(Util.KEY_ENABLE_WIDGET_CLICK)) {
            this.util.saveEnableWidgetClick(!this.enable_widget_click.isChecked());
        } else if (key.equals(Util.KEY_LOCK_SOUND)) {
            this.util.savePlayLockSound(!this.lock_sound.isChecked());
        } else if (key.equals(Util.KEY_CUSTOM_TIMEOUT)) {
            this.util.saveCustomTimeOut(Integer.parseInt(String.valueOf(objVal)));
        } else if (key.equals(Util.KEY_DELAY_LOCK)) {
            this.util.saveDelayLock(Integer.parseInt(String.valueOf(objVal)));
        }
        return true;
    }
}
