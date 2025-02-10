package com.nanoha.MyLockScreen_all;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.android.internal.telephony.ITelephony;
import com.mobclick.android.MobclickAgent;
import com.nanoha.util.AnswerCallScreen;
import com.nanoha.util.ManageKeyguard;
import com.nanoha.util.MusicControl;
import com.nanoha.util.Util;
import java.lang.reflect.Method;

public class CircleLockScreen extends Activity implements View.OnClickListener {
    public static final String CMDNAME = "command";
    public static final String CMDNEXT = "next";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDSTOP = "stop";
    int custom_time_out = 0;
    int fixDoubleInt = 0;
    private int fixDoubleLockInt = 0;
    private ITelephony iTelephony;
    boolean isPause = false;
    boolean isRingingCall = false;
    boolean isUseSecurity;
    /* access modifiers changed from: private */
    public LockScreen lockScreenView;
    private AudioManager mAudioManager;
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    CircleLockScreen.this.isRingingCall = false;
                    if (CircleLockScreen.this.lockScreenView != null) {
                        CircleLockScreen.this.lockScreenView.idealCall();
                        return;
                    }
                    return;
                case 1:
                    CircleLockScreen.this.isRingingCall = true;
                    if (CircleLockScreen.this.lockScreenView != null) {
                        CircleLockScreen.this.lockScreenView.ringingCall(incomingNumber);
                        return;
                    }
                    return;
                case 2:
                    CircleLockScreen.this.isRingingCall = false;
                    return;
                default:
                    return;
            }
        }
    };
    LinearLayout media_control_layout;
    ImageButton next;
    ImageButton play;
    ImageButton pre;
    int screen_time_out = 0;
    private TelephonyManager teleManager;
    Util util;

    private void initPhoner() {
        if (this.util.isUseCallButton()) {
            this.teleManager = (TelephonyManager) getSystemService("phone");
            this.teleManager.listen(this.mPhoneStateListener, 32);
            try {
                Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", new Class[0]);
                getITelephonyMethod.setAccessible(true);
                this.iTelephony = (ITelephony) getITelephonyMethod.invoke(this.teleManager, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void endCall() {
        try {
            this.iTelephony.endCall();
        } catch (Exception e) {
            Log.e("testABC", "End call exception=" + e.toString());
        }
    }

    public void answerCall() {
        try {
            this.iTelephony.answerRingingCall();
        } catch (Exception e) {
            Log.e("testABC", "answer call exception=" + e.toString());
            answerPhoneHeadsethook(this);
        }
    }

    private void answerPhoneHeadsethook(Context context) {
        Intent buttonDown = new Intent("android.intent.action.MEDIA_BUTTON");
        buttonDown.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
        context.sendOrderedBroadcast(buttonDown, (String) null);
        Intent buttonUp = new Intent("android.intent.action.MEDIA_BUTTON");
        buttonUp.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
        context.sendOrderedBroadcast(buttonUp, (String) null);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        this.util = Util.getInstance(this);
        this.util.saveIsLocked(true);
        this.fixDoubleLockInt = this.util.getFixDoubleLockIssue();
        String background = this.util.getBackground();
        if ("0".equals(background) && this.util.isDisplayStatusbar()) {
            setTheme(16973919);
        } else if ("0".equals(background) && !this.util.isDisplayStatusbar()) {
            setTheme(16973920);
        } else if ("0".equals(background) || this.util.isDisplayStatusbar()) {
            setTheme(16973830);
        } else {
            setTheme(16973831);
        }
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        getWindow().addFlags(524288);
        this.fixDoubleInt = this.util.getFixDoubleLockIssue();
        this.isUseSecurity = this.util.isUseSecurityLock();
        if (!this.isUseSecurity && this.fixDoubleInt == 2) {
            getWindow().addFlags(4194304);
        }
        String screenOrientation = this.util.getScreenOrientation();
        if (screenOrientation.equals(Util.SCREEN_ORIENTATION_HORIZONTAL)) {
            setRequestedOrientation(0);
        } else if (screenOrientation.equals(Util.SCREEN_ORIENTATION_VERTICAL)) {
            setRequestedOrientation(1);
        }
        this.lockScreenView = new LockScreenManager(this).createLockscreen();
        setContentView(this.lockScreenView);
        initMediaButton();
        initPhoner();
        try {
            this.screen_time_out = Settings.System.getInt(getContentResolver(), "screen_off_timeout");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        this.custom_time_out = this.util.getCustomTimeOut();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (this.custom_time_out != 0) {
            Settings.System.putInt(getContentResolver(), "screen_off_timeout", this.custom_time_out);
        }
    }

    public void initMediaButton() {
        this.media_control_layout = (LinearLayout) findViewById(R.id.media_control_layout);
        if (!((AudioManager) getSystemService("audio")).isMusicActive() || !this.util.isDisplayMusicControl()) {
            this.media_control_layout.setVisibility(8);
            return;
        }
        this.media_control_layout.setVisibility(0);
        this.pre = (ImageButton) findViewById(R.id.media_pre);
        this.play = (ImageButton) findViewById(R.id.media_play);
        this.next = (ImageButton) findViewById(R.id.media_next);
        this.pre.setOnClickListener(this);
        this.play.setOnClickListener(this);
        this.next.setOnClickListener(this);
    }

    public void onClick(View v) {
        int vid = v.getId();
        String action = "";
        switch (this.util.getMusicControl()) {
            case Util.MUSIC_CONTROL_DEFAULT /*2000*/:
                switch (vid) {
                    case R.id.media_pre:
                        action = "com.android.music.musicservicecommand.previous";
                        break;
                    case R.id.media_play:
                        action = "com.android.music.musicservicecommand.togglepause";
                        if (!this.isPause) {
                            this.isPause = true;
                            this.play.setBackgroundResource(R.drawable.ic_media_pause);
                            break;
                        } else {
                            this.isPause = false;
                            this.play.setBackgroundResource(R.drawable.ic_media_play);
                            break;
                        }
                    case R.id.media_next:
                        action = "com.android.music.musicservicecommand.next";
                        break;
                }
                Intent intent = new Intent();
                intent.setAction(action);
                sendBroadcast(intent);
                return;
            case Util.MUSIC_CONTROL_POWERAMP /*2001*/:
                switch (vid) {
                    case R.id.media_pre:
                        startService(new Intent(MusicControl.POWERAMP_ACTION_API_COMMAND).putExtra(MusicControl.POWERAMP_COMMAND, 5));
                        return;
                    case R.id.media_play:
                        startService(new Intent(MusicControl.POWERAMP_ACTION_API_COMMAND).putExtra(MusicControl.POWERAMP_COMMAND, 1));
                        if (this.isPause) {
                            this.isPause = false;
                            this.play.setBackgroundResource(R.drawable.ic_media_play);
                            return;
                        }
                        this.isPause = true;
                        this.play.setBackgroundResource(R.drawable.ic_media_pause);
                        return;
                    case R.id.media_next:
                        startService(new Intent(MusicControl.POWERAMP_ACTION_API_COMMAND).putExtra(MusicControl.POWERAMP_COMMAND, 4));
                        return;
                    default:
                        return;
                }
            case Util.MUSIC_CONTROL_PLAYERPRO /*2002*/:
                switch (vid) {
                    case R.id.media_pre:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_PRE));
                        return;
                    case R.id.media_play:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_TOGGLE));
                        if (this.isPause) {
                            this.isPause = false;
                            this.play.setBackgroundResource(R.drawable.ic_media_play);
                            return;
                        }
                        this.isPause = true;
                        this.play.setBackgroundResource(R.drawable.ic_media_pause);
                        return;
                    case R.id.media_next:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_NEXT));
                        return;
                    default:
                        return;
                }
            case Util.MUSIC_CONTROL_PLAYERPRO_TRIAL /*2003*/:
                switch (vid) {
                    case R.id.media_pre:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_PRE_TRAIL));
                        return;
                    case R.id.media_play:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_TOGGLE_TRAIL));
                        if (this.isPause) {
                            this.isPause = false;
                            this.play.setBackgroundResource(R.drawable.ic_media_play);
                            return;
                        }
                        this.isPause = true;
                        this.play.setBackgroundResource(R.drawable.ic_media_pause);
                        return;
                    case R.id.media_next:
                        sendBroadcast(new Intent(MusicControl.PLAYERPRO_NEXT_TRAIL));
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    public void collapseStatusbar() {
        int i = 0;
        Object mStatusbar = getSystemService("statusbar");
        Method[] methods = mStatusbar.getClass().getMethods();
        Method collapseMethod = null;
        int length = methods.length;
        while (true) {
            if (i >= length) {
                break;
            }
            Method method = methods[i];
            if (method.getName().compareTo("collapse") == 0) {
                collapseMethod = method;
                break;
            }
            i++;
        }
        try {
            collapseMethod.invoke(mStatusbar, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.util.isDisableStatusbar() && !hasFocus) {
            collapseStatusbar();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void disableStatusbar() {
        int i = 0;
        Object mStatusbar = getSystemService("statusbar");
        Method[] methods = mStatusbar.getClass().getMethods();
        Method disableMethod = null;
        int length = methods.length;
        while (true) {
            if (i >= length) {
                break;
            }
            Method method = methods[i];
            if (method.getName().compareTo("disable") == 0) {
                disableMethod = method;
                break;
            }
            i++;
        }
        try {
            disableMethod.invoke(mStatusbar, new Object[]{1});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int i;
        if (event.getAction() == 0) {
            switch (keyCode) {
                case 24:
                case 25:
                    synchronized (this) {
                        if (this.mAudioManager == null) {
                            this.mAudioManager = (AudioManager) getSystemService("audio");
                        }
                    }
                    if (this.mAudioManager.isMusicActive()) {
                        AudioManager audioManager = this.mAudioManager;
                        if (keyCode == 24) {
                            i = 1;
                        } else {
                            i = -1;
                        }
                        audioManager.adjustStreamVolume(3, i, 0);
                    }
                    return true;
                case 79:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON", (Uri) null);
                    intent.putExtra("android.intent.extra.KEY_EVENT", event);
                    sendOrderedBroadcast(intent, (String) null);
                    return true;
            }
        } else if (event.getAction() == 1) {
            switch (keyCode) {
                case 79:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                    Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON", (Uri) null);
                    intent2.putExtra("android.intent.extra.KEY_EVENT", event);
                    sendOrderedBroadcast(intent2, (String) null);
                    return true;
            }
        }
        if (keyCode == 3) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.lockScreenView.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.lockScreenView.onStop();
        if (this.custom_time_out != 0) {
            Settings.System.putInt(getContentResolver(), "screen_off_timeout", this.screen_time_out);
        }
        if (this.isRingingCall) {
            startActivity(new Intent(this, AnswerCallScreen.class));
            this.isRingingCall = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.lockScreenView.onDestory();
        if (this.fixDoubleLockInt == 0) {
            ManageKeyguard.exitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
        }
        if (this.teleManager != null) {
            this.teleManager.listen(this.mPhoneStateListener, 0);
        }
        this.lockScreenView = null;
        this.mAudioManager = null;
        this.iTelephony = null;
        this.teleManager = null;
        sendBroadcast(new Intent(Util.BROADCAST_UNLOCKED));
        this.util = null;
        System.gc();
    }
}
