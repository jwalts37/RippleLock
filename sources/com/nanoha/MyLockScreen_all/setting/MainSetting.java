package com.nanoha.MyLockScreen_all.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.nanoha.CropImage.CropImage;
import com.nanoha.MyLockScreen_all.MyLockScreen;
import com.nanoha.MyLockScreen_all.MyLockService;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.MyLockScreen_all.SelectWidgetActivity;
import com.nanoha.util.Util;

public class MainSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    static final int REQUEST_CROP_IMG = 102;
    static final int REQUEST_SELECT_FONT = 103;
    static final int REQUEST_SELEC_BACKGROUND = 101;
    Preference add_widget;
    CheckBoxPreference always_display_battery;
    EditTextPreference background_alpha_setting;
    Preference background_setting;
    CheckBoxPreference bootup_auto_execute;
    ListPreference date_format;
    CheckBoxPreference disable_statusbar;
    CheckBoxPreference display_carrier;
    CheckBoxPreference display_lock_icon;
    CheckBoxPreference display_music_control;
    CheckBoxPreference display_mute_icon;
    CheckBoxPreference display_statusbar;
    ListPreference fix_double_lock_issue;
    CheckBoxPreference fix_power_button_issue;
    Preference font_color;
    Preference font_size;
    ListPreference font_style;
    boolean hasAd = false;
    ListPreference music_control;
    CheckBoxPreference only_display_lock_circle;
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
        addPreferencesFromResource(R.xml.main_setting);
        this.util = Util.getInstance(this);
        initView();
    }

    private void initView() {
        this.font_size = findPreference("font_size");
        this.font_color = findPreference("font_color");
        this.background_setting = findPreference(Util.KEY_BACKGROUND);
        this.shortcut_setting = findPreference(Util.KEY_SHORTCUT_SETTING);
        this.password_setting = findPreference("password_setting");
        this.add_widget = findPreference("add_widget");
        this.use_system_security_lock = (CheckBoxPreference) findPreference(Util.KEY_USE_SYSTEM_SECURITY_LOCK);
        this.screen_orientation = (ListPreference) findPreference(Util.KEY_SCREEN_ORIENTATION);
        this.display_lock_icon = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_LOCK_ICON);
        this.display_carrier = (CheckBoxPreference) findPreference("display_carrier");
        this.display_statusbar = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_STATUSBAR);
        this.disable_statusbar = (CheckBoxPreference) findPreference(Util.KEY_DISABLE_STATUSBAR);
        this.only_display_lock_circle = (CheckBoxPreference) findPreference(Util.KEY_ONLY_DISPLAY_LOCK_CIRCLE);
        this.unlock_anim = (ListPreference) findPreference(Util.KEY_UNLOCK_ANIM);
        this.font_style = (ListPreference) findPreference(Util.KEY_FONT_STYLE);
        this.date_format = (ListPreference) findPreference("date_format");
        this.fix_double_lock_issue = (ListPreference) findPreference(Util.KEY_FIX_DOUBLE_LOCK_ISSUE);
        this.unlock_vibrate = (CheckBoxPreference) findPreference(Util.KEY_UNLOCK_VIBRATE);
        this.always_display_battery = (CheckBoxPreference) findPreference(Util.KEY_ALWAYS_DISPLAY_BATTERY);
        this.display_mute_icon = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_MUTE_ICON);
        this.display_music_control = (CheckBoxPreference) findPreference(Util.KEY_DISPLAY_MUSIC_CONTROL);
        this.background_alpha_setting = (EditTextPreference) findPreference(Util.KEY_BACKGROUND_ALPHA_SETTING);
        this.use_password = (CheckBoxPreference) findPreference(Util.KEY_USE_PASSWORD);
        this.bootup_auto_execute = (CheckBoxPreference) findPreference(Util.KEY_BOOTUP_AUTO_EXECUTE);
        this.fix_power_button_issue = (CheckBoxPreference) findPreference(Util.KEY_FIX_POWER_BUTTON_ISSUE);
        this.music_control = (ListPreference) findPreference(Util.KEY_MUSIC_CONTROL);
        this.use_call_button = (CheckBoxPreference) findPreference(Util.KEY_USE_CALL_BUTTON);
        this.use_call_button.setOnPreferenceChangeListener(this);
        this.use_system_security_lock.setOnPreferenceChangeListener(this);
        this.music_control.setOnPreferenceChangeListener(this);
        this.fix_power_button_issue.setOnPreferenceChangeListener(this);
        this.bootup_auto_execute.setOnPreferenceChangeListener(this);
        this.use_password.setOnPreferenceChangeListener(this);
        this.background_alpha_setting.setOnPreferenceChangeListener(this);
        this.display_music_control.setOnPreferenceChangeListener(this);
        this.display_mute_icon.setOnPreferenceChangeListener(this);
        this.only_display_lock_circle.setOnPreferenceChangeListener(this);
        this.always_display_battery.setOnPreferenceChangeListener(this);
        this.unlock_vibrate.setOnPreferenceChangeListener(this);
        this.display_lock_icon.setOnPreferenceChangeListener(this);
        this.screen_orientation.setOnPreferenceChangeListener(this);
        this.background_setting.setOnPreferenceChangeListener(this);
        this.display_carrier.setOnPreferenceChangeListener(this);
        this.display_statusbar.setOnPreferenceChangeListener(this);
        this.unlock_anim.setOnPreferenceChangeListener(this);
        this.disable_statusbar.setOnPreferenceChangeListener(this);
        this.font_style.setOnPreferenceChangeListener(this);
        this.date_format.setOnPreferenceChangeListener(this);
        this.fix_double_lock_issue.setOnPreferenceChangeListener(this);
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
        if (this.util.isDisableStatusbar()) {
            this.disable_statusbar.setChecked(true);
        } else {
            this.disable_statusbar.setChecked(false);
        }
        if (this.util.isDisplayLockIcon()) {
            this.display_lock_icon.setChecked(true);
        } else {
            this.display_lock_icon.setChecked(false);
        }
        if (this.util.isUnlockVibrate()) {
            this.unlock_vibrate.setChecked(true);
        } else {
            this.unlock_vibrate.setChecked(false);
        }
        if (this.util.isDisplayBattery()) {
            this.always_display_battery.setChecked(true);
        } else {
            this.always_display_battery.setChecked(false);
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
        } else {
            this.only_display_lock_circle.setChecked(false);
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
        if (this.util.isFixPowerButtonIssue()) {
            this.fix_power_button_issue.setChecked(true);
        } else {
            this.fix_power_button_issue.setChecked(false);
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
        EditText alphaText = this.background_alpha_setting.getEditText();
        alphaText.setText(String.valueOf(this.util.getBackgroundAlpha()));
        alphaText.setInputType(2);
        if (this.hasAd) {
            this.font_size.setEnabled(false);
            this.font_color.setEnabled(false);
            this.background_setting.setEnabled(false);
            this.unlock_anim.setEnabled(false);
            this.date_format.setEnabled(false);
            this.use_password.setEnabled(false);
            this.password_setting.setEnabled(false);
            this.background_alpha_setting.setEnabled(false);
            this.music_control.setEnabled(false);
            this.add_widget.setEnabled(false);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.font_size) {
            Intent fontSizeIntent = new Intent();
            fontSizeIntent.setClass(this, FontSizeSetting.class);
            startActivity(fontSizeIntent);
            return false;
        } else if (preference == this.font_color) {
            Intent fontColorIntent = new Intent();
            fontColorIntent.setClass(this, FontColorSetting.class);
            startActivity(fontColorIntent);
            return false;
        } else if (preference == this.shortcut_setting) {
            Intent shortcutIntent = new Intent();
            shortcutIntent.setClass(this, ShortcutSetting.class);
            startActivity(shortcutIntent);
            return false;
        } else if (preference == this.add_widget) {
            new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon).setMessage(R.string.add_widget_hint).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent selectWidgetIntent = new Intent();
                    selectWidgetIntent.setClass(MainSetting.this, SelectWidgetActivity.class);
                    MainSetting.this.startActivity(selectWidgetIntent);
                }
            }).show();
            return false;
        } else if (preference != this.password_setting) {
            return false;
        } else {
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
                            MainSetting.this.util.savePassword(newPassword);
                            Util.toastMessage(MainSetting.this, R.string.password_set_success);
                            return;
                        }
                        Util.toastMessage(MainSetting.this, R.string.two_password_not);
                        return;
                    }
                    Util.toastMessage(MainSetting.this, R.string.ori_password_error);
                }
            });
            builder.setNegativeButton(getString(R.string.no), (DialogInterface.OnClickListener) null);
            builder.create();
            builder.show();
            return false;
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
        } else if (requestCode == 103 && resultCode == -1) {
            String path = data.getData().toString().replace("file://", "");
            Log.e("testABC", "font path=" + path);
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
        boolean isUse;
        boolean isUse2;
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
        } else if (key.equals(Util.KEY_SCREEN_ORIENTATION)) {
            this.util.saveScreenOrientation(String.valueOf(objVal));
        } else if (key.equals("display_carrier")) {
            this.util.saveDisplayCarrier(!this.display_carrier.isChecked());
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
        } else if (key.equals(Util.KEY_DISPLAY_STATUSBAR)) {
            this.util.saveDisplayStatusbar(!this.display_statusbar.isChecked());
        } else if (key.equals(Util.KEY_DISABLE_STATUSBAR)) {
            this.util.saveDisableStatusbar(!this.disable_statusbar.isChecked());
        } else if (key.equals(Util.KEY_FONT_STYLE)) {
            int intVal = Integer.parseInt(String.valueOf(objVal));
            if (intVal == 5) {
                Intent intent2 = new Intent();
                intent2.setType("file/*");
                intent2.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(intent2, 103);
            } else {
                this.util.saveFontStyle(intVal);
            }
        } else if (key.equals(Util.KEY_DISPLAY_LOCK_ICON)) {
            this.util.saveDisplayLockIcon(!this.display_lock_icon.isChecked());
        } else if (key.equals(Util.KEY_BOOTUP_AUTO_EXECUTE)) {
            this.util.saveBootupAutoExecute(!this.bootup_auto_execute.isChecked());
        } else if (key.equals(Util.KEY_UNLOCK_VIBRATE)) {
            this.util.saveUnlockVibrate(!this.unlock_vibrate.isChecked());
        } else if (key.equals(Util.KEY_ALWAYS_DISPLAY_BATTERY)) {
            this.util.saveDisplayBattery(!this.always_display_battery.isChecked());
        } else if (key.equals("date_format")) {
            this.util.saveDateFormat(String.valueOf(objVal));
        } else if (key.equals(Util.KEY_DISPLAY_MUTE_ICON)) {
            this.util.saveDisplayMuteIcon(!this.display_mute_icon.isChecked());
        } else if (key.equals(Util.KEY_DISPLAY_MUSIC_CONTROL)) {
            this.util.saveDisplayMusicControl(!this.display_music_control.isChecked());
        } else if (key.equals(Util.KEY_ONLY_DISPLAY_LOCK_CIRCLE)) {
            this.util.saveOnlyDisplayLockCircle(!this.only_display_lock_circle.isChecked());
        } else if (key.equals(Util.KEY_FIX_POWER_BUTTON_ISSUE)) {
            this.util.saveFixPowerButtonIssue(!this.fix_power_button_issue.isChecked());
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
        } else if (key.equals(Util.KEY_BACKGROUND_ALPHA_SETTING)) {
            if (objVal == null || "".equals(objVal)) {
                objVal = "0";
            }
            int alpha = Integer.parseInt(String.valueOf(objVal));
            if (alpha > 200) {
                alpha = 200;
            }
            this.util.saveBackgroundAlpha(alpha);
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
        }
        return true;
    }
}
