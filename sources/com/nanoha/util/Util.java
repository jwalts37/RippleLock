package com.nanoha.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.MyLockScreen_all.WidgetLayout;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static final String BROADCAST_UNLOCKED = "com.nanoha.UNLOCKED";
    public static final String DISPLAY_CHANGELOG = "display_changelog";
    public static final String IN_HOME_WIDZARD = "in_home_widzard";
    public static final String IS_FIRST_RUN = "is_first_run";
    public static final String KEY_ALWAYS_DISPLAY_BATTERY = "always_display_battery";
    public static final String KEY_AM_COLOR = "am_color";
    public static final String KEY_AM_SIZE = "am_size";
    public static final String KEY_BACKGROUND = "background_setting";
    public static final String KEY_BACKGROUND_ALPHA_SETTING = "background_alpha_setting";
    public static final String KEY_BLOCK_HOME_SETTING = "block_home_key_setting";
    public static final String KEY_BOOTUP_AUTO_EXECUTE = "bootup_auto_execute";
    public static final String KEY_BOTTOM_SHORTCUT_SETTING = "bottom_shortcut_setting";
    public static final String KEY_CARRIER_COLOR = "carrier_color";
    public static final String KEY_CARRIER_SIZE = "carrier_size";
    public static final String KEY_COLOR_AUTO = "default_color";
    public static final String KEY_CUSTOM_FONT = "custom_font";
    public static final String KEY_CUSTOM_TIMEOUT = "custom_timeout";
    public static final String KEY_DATE_COLOR = "date_color";
    public static final String KEY_DATE_FORMAT = "date_format";
    public static final String KEY_DATE_SIZE = "date_size";
    public static final String KEY_DELAY_LOCK = "delay_lock";
    public static final String KEY_DISABLE_STATUSBAR = "disable_statusbar";
    public static final String KEY_DISPLAY_AD = "display_ad";
    public static final String KEY_DISPLAY_CARRIER = "display_carrier";
    public static final String KEY_DISPLAY_DATE = "display_date";
    public static final String KEY_DISPLAY_LOCK_ICON = "display_lock_icon";
    public static final String KEY_DISPLAY_MUSIC_CONTROL = "display_music_control";
    public static final String KEY_DISPLAY_MUTE_ICON = "display_mute_icon";
    public static final String KEY_DISPLAY_QUICKVIEW = "display_quickview";
    public static final String KEY_DISPLAY_STATUS = "display_status";
    public static final String KEY_DISPLAY_STATUSBAR = "display_statusbar";
    public static final String KEY_DISPLAY_TIME = "display_time";
    public static final String KEY_ENABLE_WIDGET_CLICK = "enable_widget_click";
    public static final String KEY_FIX_DOUBLE_LOCK_ISSUE = "fix_double_lock_issue";
    public static final String KEY_FIX_POWER_BUTTON_ISSUE = "fix_power_button_issue";
    public static final String KEY_FONT_STYLE = "font_style";
    public static final String KEY_ISLOCKED = "is_locked";
    public static final String KEY_LEFT_SHORTCUT_SETTING = "left_shortcut_setting";
    public static final String KEY_LOCK_SOUND = "lock_sound";
    public static final String KEY_MAX_VERION = "max_version";
    public static final String KEY_MUSIC_CONTROL = "music_control";
    public static final String KEY_ONLY_DISPLAY_LOCK_CIRCLE = "only_display_lock_circle";
    public static final String KEY_REGIST_VERSION = "display_lock";
    public static final String KEY_RIGHT_SHORTCUT_SETTING = "right_shortcut_setting";
    public static final String KEY_SCREEN_ORIENTATION = "screen_orientation";
    public static final String KEY_SET_DOUBLE_MODE = "setDoubleMode";
    public static final String KEY_SHORTCUT_SETTING = "shortcut_setting";
    public static final String KEY_SHORTCU_ICON = "_icon";
    public static final String KEY_SIZE_AUTO = "size_screen_adapt";
    public static final String KEY_STATUS_COLOR = "status_color";
    public static final String KEY_STATUS_SIZE = "status_size";
    public static final String KEY_THEME = "custom_theme";
    public static final String KEY_TIME_COLOR = "time_color";
    public static final String KEY_TIME_SIZE = "time_size";
    public static final String KEY_TOP_SHORTCUT_SETTING = "top_shortcut_setting";
    public static final String KEY_UNLOCK_ANIM = "unlock_anim";
    public static final String KEY_UNLOCK_VIBRATE = "unlock_vibrate";
    public static final String KEY_USE_CALL_BUTTON = "use_call_button";
    public static final String KEY_USE_PASSWORD = "use_password";
    public static final String KEY_USE_SYSTEM_SECURITY_LOCK = "use_system_security_lock";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final String KEY_WIDGETINFO = "key_widgetinfo";
    public static final int MUSIC_CONTROL_DEFAULT = 2000;
    public static final int MUSIC_CONTROL_PLAYERPRO = 2002;
    public static final int MUSIC_CONTROL_PLAYERPRO_TRIAL = 2003;
    public static final int MUSIC_CONTROL_POWERAMP = 2001;
    public static final String OMNIPOTENT_PASSWORD = "2012";
    public static final int PASSWORD_MAX_COUNT = 20;
    public static final String PREF_PASSWORD = "pref_password";
    public static final String PREF_START_COUNT = "start_count";
    public static final String QUICK_INTENT_STRING = "quick_intent_string";
    public static final int REGIST_VALUE = 2004;
    public static final String SCREEN_ORIENTATION_HORIZONTAL = "horizontal";
    public static final String SCREEN_ORIENTATION_ROTATE = "rotate";
    public static final String SCREEN_ORIENTATION_VERTICAL = "vertical";
    public static final String SETTING_PREF = "setting_pref";
    public static final int START_ADS_COUNT = 1;
    public static final String SYSTEM_TIMEOUT = "system_timeout";
    public static final String USE_LAUNCHER_CLASS_NAME = "use_launcher_class_name";
    public static final String USE_LAUNCHER_PACKAGE_NAME = "use_launcher_package_name";
    public static int hasAd = -1;
    private static Util utilInstance;
    private Context mContext;

    public static Util getInstance(Context context) {
        if (utilInstance == null) {
            utilInstance = new Util(context);
        }
        return utilInstance;
    }

    private Util(Context context) {
        this.mContext = context;
    }

    public boolean isLocked() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_ISLOCKED, false);
    }

    public void saveIsLocked(boolean isLocked) {
        Log.e("nanoha", "saveIsLocked:" + isLocked);
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_ISLOCKED, isLocked);
        editor.commit();
    }

    public void saveValue(String key, int value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getValue(String key, int defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(key, defaultValue);
    }

    public void saveValue(String key, String value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key, String defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(key, defaultValue);
    }

    public void saveValue(String key, boolean value) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getValue(String key, boolean defaultValue) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(key, defaultValue);
    }

    public void saveDelayLock(int delayTime) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_DELAY_LOCK, delayTime);
        editor.commit();
    }

    public int getDelayLock() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_DELAY_LOCK, 0);
    }

    public void saveCustomTimeOut(int timeout) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_CUSTOM_TIMEOUT, timeout);
        editor.commit();
    }

    public int getCustomTimeOut() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_CUSTOM_TIMEOUT, 0);
    }

    public void saveSystemTimeOut(int timeout) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(SYSTEM_TIMEOUT, timeout);
        editor.commit();
    }

    public int getSystemTimeOut() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(SYSTEM_TIMEOUT, 60000);
    }

    public void savePlayLockSound(boolean isPlay) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_LOCK_SOUND, isPlay);
        editor.commit();
    }

    public boolean isPlayLockSound() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_LOCK_SOUND, true);
    }

    public void registFreeVersion() {
        try {
            if (new DeviceId(this.mContext).utilRegist()) {
                SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
                editor.putInt("display_lock", 2004);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e("testABC", "registFreeVersion e=" + e.toString());
        }
    }

    public boolean checkHasAd() {
        try {
            if (this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("display_lock", 0) == 2004) {
                return false;
            }
            if (new DeviceId(this.mContext).checkHasAd()) {
                return true;
            }
            registFreeVersion();
            return true;
        } catch (Exception e) {
            Exception exc = e;
            return false;
        }
    }

    public void saveDisplayDate(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean("display_date", display);
        editor.commit();
    }

    public void saveDisplayStatus(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_STATUS, display);
        editor.commit();
    }

    public boolean isDisplayStatus() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_STATUS, true);
    }

    public boolean isDisplayDate() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean("display_date", true);
    }

    public void saveDisplayTime(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean("display_time", display);
        editor.commit();
    }

    public boolean isDisplayTime() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean("display_time", true);
    }

    public void saveEnableWidgetClick(boolean enableClick) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_ENABLE_WIDGET_CLICK, enableClick);
        editor.commit();
    }

    public boolean isEnableWidgetClick() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_ENABLE_WIDGET_CLICK, true);
    }

    public boolean isUseCallButton() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_USE_CALL_BUTTON, false);
    }

    public void saveUseCallButton(boolean isUse) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_USE_CALL_BUTTON, isUse);
        editor.commit();
    }

    public void saveDisplayChangelog(boolean display, int versionCode) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(DISPLAY_CHANGELOG, display);
        editor.putInt(KEY_VERSION_CODE, versionCode);
        editor.commit();
    }

    public boolean isDisplayChangelog(int versionCode) {
        SharedPreferences pref = this.mContext.getSharedPreferences(SETTING_PREF, 2);
        boolean isDisplay = pref.getBoolean(DISPLAY_CHANGELOG, true);
        if (isDisplay || versionCode <= pref.getInt(KEY_VERSION_CODE, 0)) {
            return isDisplay;
        }
        return true;
    }

    public int getAndInitStartCount() {
        SharedPreferences sp = this.mContext.getSharedPreferences(SETTING_PREF, 2);
        int count = sp.getInt(PREF_START_COUNT, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREF_START_COUNT, count + 1);
        editor.commit();
        return count;
    }

    public List<WidgetInfo> loadWidgetInfo() {
        List<WidgetInfo> widgetInfoList = new ArrayList<>();
        String saveStr = this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(KEY_WIDGETINFO, (String) null);
        if (saveStr != null) {
            String[] infos = saveStr.split(";");
            for (String split : infos) {
                String[] widgetInfoStr = split.split(",");
                if (widgetInfoStr.length == 5) {
                    WidgetInfo info = new WidgetInfo();
                    info.appWidgetId = Integer.parseInt(widgetInfoStr[0]);
                    info.x = Integer.parseInt(widgetInfoStr[1]);
                    info.y = Integer.parseInt(widgetInfoStr[2]);
                    if (info.lp == null) {
                        info.lp = new WidgetLayout.LayoutParams(0, 0);
                    }
                    info.lp.width = Integer.parseInt(widgetInfoStr[3]);
                    info.lp.height = Integer.parseInt(widgetInfoStr[4]);
                    widgetInfoList.add(info);
                }
            }
        }
        return widgetInfoList;
    }

    public void saveWidgetInfo(List<WidgetInfo> widgetInfoList) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        StringBuffer saveStr = new StringBuffer();
        for (int i = 0; i < widgetInfoList.size(); i++) {
            WidgetInfo info = widgetInfoList.get(i);
            saveStr.append(info.appWidgetId);
            saveStr.append(",");
            saveStr.append(info.x);
            saveStr.append(",");
            saveStr.append(info.y);
            saveStr.append(",");
            saveStr.append(info.lp.width);
            saveStr.append(",");
            saveStr.append(info.lp.height);
            saveStr.append(";");
        }
        editor.putString(KEY_WIDGETINFO, saveStr.toString());
        editor.commit();
    }

    public class WidgetInfo {
        public int appWidgetId;
        public WidgetLayout.LayoutParams lp;
        public int x;
        public int y;

        public WidgetInfo() {
        }
    }

    public static void toastMessage(Context context, int resId) {
        Toast.makeText(context, resId, 0).show();
    }

    public void savePassword(String password) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.commit();
    }

    public boolean hasPassword() {
        return !getPassword().equals("");
    }

    public String getPassword() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(PREF_PASSWORD, "");
    }

    public boolean isRightPassword(String checkPassword) {
        String oriPasswrod = getPassword();
        if (oriPasswrod.equals("") || checkPassword.equals(oriPasswrod) || checkPassword.equals(OMNIPOTENT_PASSWORD)) {
            return true;
        }
        return false;
    }

    public void saveDisplayCarrier(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean("display_carrier", display);
        editor.commit();
    }

    public void saveFixPowerButtonIssue(boolean isFix) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_FIX_POWER_BUTTON_ISSUE, isFix);
        editor.commit();
    }

    public void saveUseSecurityLock(boolean isUse) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_USE_SYSTEM_SECURITY_LOCK, isUse);
        editor.commit();
    }

    public void saveUsePassword(boolean isUse) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_USE_PASSWORD, isUse);
        editor.commit();
    }

    public void saveOnlyDisplayLockCircle(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_ONLY_DISPLAY_LOCK_CIRCLE, display);
        editor.commit();
    }

    public void saveDisplayMuteIcon(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_MUTE_ICON, display);
        editor.commit();
    }

    public void saveBootupAutoExecute(boolean isAuto) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_BOOTUP_AUTO_EXECUTE, isAuto);
        editor.commit();
    }

    public void saveDisplayMusicControl(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_MUSIC_CONTROL, display);
        editor.commit();
    }

    public void saveBackgroundAlpha(int alpha) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_BACKGROUND_ALPHA_SETTING, alpha);
        editor.commit();
    }

    public void saveMusicControl(int control) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_MUSIC_CONTROL, control);
        editor.commit();
    }

    public void saveDateFormat(String format) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString("date_format", format);
        editor.commit();
    }

    public void saveDisplayQuickView(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_QUICKVIEW, display);
        editor.commit();
    }

    public void saveDisplayBattery(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_ALWAYS_DISPLAY_BATTERY, display);
        editor.commit();
    }

    public void saveDisplayStatusbar(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_STATUSBAR, display);
        editor.commit();
    }

    public void saveUnlockVibrate(boolean isVibrate) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_UNLOCK_VIBRATE, isVibrate);
        editor.commit();
    }

    public void saveDisplayLockIcon(boolean display) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISPLAY_LOCK_ICON, display);
        editor.commit();
    }

    public void saveDisableStatusbar(boolean isDisable) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(KEY_DISABLE_STATUSBAR, isDisable);
        editor.commit();
    }

    public void saveUnlockAnim(Integer unlock_anim) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_UNLOCK_ANIM, unlock_anim.intValue());
        editor.commit();
    }

    public void saveFixDoubleLockIssue(Integer mode) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_FIX_DOUBLE_LOCK_ISSUE, mode.intValue());
        editor.commit();
    }

    public void saveScreenOrientation(String orientation) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(KEY_SCREEN_ORIENTATION, orientation);
        editor.commit();
    }

    public void saveFontColor(String key, int color) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(key, color);
        editor.commit();
    }

    public void saveFontStyle(int style) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(KEY_FONT_STYLE, style);
        editor.commit();
    }

    public void saveIsAuto(String key, boolean isAuto) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putBoolean(key, isAuto);
        editor.commit();
    }

    public void saveBackground(String uri) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(KEY_BACKGROUND, uri);
        editor.commit();
    }

    public void saveCustomFontPath(String path) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(KEY_CUSTOM_FONT, path);
        editor.commit();
    }

    public String getCustomFontPath() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(KEY_CUSTOM_FONT, (String) null);
    }

    public void saveShortcutSetting(String key, String intentString) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(key, intentString);
        editor.commit();
    }

    public void saveShortcutIcon(String key, String iconUriString) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putString(key, iconUriString);
        editor.commit();
    }

    public String getBackground() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(KEY_BACKGROUND, "0");
    }

    public int getBackgroundAlpha() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_BACKGROUND_ALPHA_SETTING, 0);
    }

    public String getShortcutSetting(String key) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(key, "-1");
    }

    public String getShortcutIcon(String key) {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(key, "-1");
    }

    public String getDateformat() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString("date_format", "0");
    }

    public String getScreenOrientation() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getString(KEY_SCREEN_ORIENTATION, SCREEN_ORIENTATION_ROTATE);
    }

    public void saveFontSize(String key, int size) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SETTING_PREF, 2).edit();
        editor.putInt(key, size);
        editor.commit();
    }

    public boolean isSizeAuto() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_SIZE_AUTO, true);
    }

    public boolean isUnlockVibrate() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_UNLOCK_VIBRATE, true);
    }

    public boolean isDisplayQuickView() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_QUICKVIEW, true);
    }

    public boolean isUsePassword() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_USE_PASSWORD, false);
    }

    public boolean isUseSecurityLock() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_USE_SYSTEM_SECURITY_LOCK, false);
    }

    public boolean isFixPowerButtonIssue() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_FIX_POWER_BUTTON_ISSUE, false);
    }

    public boolean isOnlyDisplayLockCircle() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_ONLY_DISPLAY_LOCK_CIRCLE, false);
    }

    public boolean isDisplayMusicControl() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_MUSIC_CONTROL, true);
    }

    public boolean isBootupAutoExecute() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_BOOTUP_AUTO_EXECUTE, true);
    }

    public boolean isDisplayLockIcon() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_LOCK_ICON, true);
    }

    public boolean isDisplayMuteIcon() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_MUTE_ICON, false);
    }

    public boolean isDisplayBattery() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_ALWAYS_DISPLAY_BATTERY, false);
    }

    public boolean isDisplayCarrier() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean("display_carrier", true);
    }

    public boolean isColorAuto() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_COLOR_AUTO, true);
    }

    public boolean isDisableStatusbar() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISABLE_STATUSBAR, false);
    }

    public boolean isDisplayStatusbar() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getBoolean(KEY_DISPLAY_STATUSBAR, true);
    }

    public int getTimeSize() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("time_size", 0);
    }

    public int getFixDoubleLockIssue() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_FIX_DOUBLE_LOCK_ISSUE, 3);
    }

    public int getAmSize() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_AM_SIZE, 0);
    }

    public int getMusicControl() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_MUSIC_CONTROL, MUSIC_CONTROL_DEFAULT);
    }

    public int getFontStyle() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_FONT_STYLE, 0);
    }

    public int getUnlcokAnim() {
        switch (this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_UNLOCK_ANIM, 0)) {
            case 1:
                return R.anim.fade;
            case 2:
                return R.anim.push_right_out;
            case 3:
                return R.anim.push_left_out;
            case 4:
                return R.anim.push_bottom_out;
            case 5:
                return R.anim.push_top_out;
            default:
                return R.anim.fade;
        }
    }

    public int getDateSize() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("date_size", 0);
    }

    public int getStatusSize() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_STATUS_SIZE, 0);
    }

    public int getCarrierSize() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("carrier_size", 0);
    }

    public int getTimeColor() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("time_color", -1);
    }

    public int getAmColor() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_AM_COLOR, -1);
    }

    public int getDateColor() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("date_color", -1);
    }

    public int getStatusColor() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt(KEY_STATUS_COLOR, -1);
    }

    public int getCarrierColor() {
        return this.mContext.getSharedPreferences(SETTING_PREF, 2).getInt("carrier_color", -1);
    }
}
