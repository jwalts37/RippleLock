package com.nanoha.MyLockScreen_all;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.feedback.UMFeedbackService;
import com.mobclick.android.MobclickAgent;
import com.nanoha.MyLockScreen_all.setting.RippleLockSetting;
import com.nanoha.util.Util;
import java.util.Timer;
import java.util.TimerTask;

public class MyLockScreen extends Activity implements View.OnClickListener {
    public static final int ADLAYOUT_INVISIBLE = 105;
    public static final int ADLAYOUT_VISIBLE = 104;
    public static final int GET_PUSHAD = 102;
    public static final int MSG_INIT_DISPLAY_AD = 103;
    public static final String MYLOCKSCREEN_PREF = "mylockscreen_pref";
    public static final String PREF_STATE = "pref_state";
    public static final int REMOVE_AD = 101;
    public static final int SPEND_POINT = 220;
    public static final int STATE_START = 1;
    public static final int STATE_STOP = 2;
    static final int UPDATE_DISPLAYAD = 909;
    public static final int VERSION_CODE = 42;
    public static boolean hasAd = true;
    LinearLayout AdLinearLayout;
    LinearLayout adLayout;
    View adView;
    /* access modifiers changed from: private */
    public Button contorlButton;
    boolean displayAd = false;
    String displayText;
    private Button email_to_me;
    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyLockScreen.REMOVE_AD /*101*/:
                    MyLockScreen.this.adLayout.setVisibility(4);
                    MyLockScreen.this.remov_ad_text.setVisibility(0);
                    MyLockScreen.hasAd = false;
                    return;
                case 102:
                    Log.d("nanoha", "Get PushAd");
                    return;
                case MyLockScreen.MSG_INIT_DISPLAY_AD /*103*/:
                    MyLockScreen.this.initDisplayAd();
                    return;
                case MyLockScreen.ADLAYOUT_VISIBLE /*104*/:
                    MyLockScreen.this.adLayout.setVisibility(0);
                    return;
                case MyLockScreen.ADLAYOUT_INVISIBLE /*105*/:
                    MyLockScreen.this.adLayout.setVisibility(4);
                    return;
                case MyLockScreen.UPDATE_DISPLAYAD /*909*/:
                    MyLockScreen.this.updateResultsInUi();
                    return;
                default:
                    return;
            }
        }
    };
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (MyLockScreen.this.pointsTextView != null && MyLockScreen.this.update_text) {
                MyLockScreen.this.pointsTextView.setText(MyLockScreen.this.displayText);
                MyLockScreen.this.update_text = false;
            }
        }
    };
    TextView pointsTextView;
    int prePoint;
    TextView remov_ad_text;
    private Button settinglButton;
    public int state;
    TimerTask task = new TimerTask() {
        public void run() {
            if (MyLockScreen.hasAd) {
                MyLockScreen.this.mHandler.sendEmptyMessage(102);
            }
        }
    };
    Timer timer = new Timer();
    boolean update_display_ad;
    boolean update_text = false;
    public Util util;

    /* access modifiers changed from: private */
    public void initDisplayAd() {
        hasAd = false;
        this.adLayout = (LinearLayout) findViewById(R.id.adLayout);
        this.AdLinearLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
        this.remov_ad_text = (TextView) findViewById(R.id.remov_ad_text);
        this.adLayout.setVisibility(4);
        this.remov_ad_text.setVisibility(4);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.onError(this);
        MobclickAgent.setUpdateOnlyWifi(false);
        MobclickAgent.update(this);
        this.util = Util.getInstance(this);
        setContentView(R.layout.main);
        this.state = getSharedPreferences(MYLOCKSCREEN_PREF, 2).getInt(PREF_STATE, 2);
        this.email_to_me = (Button) findViewById(R.id.email_to_me);
        this.email_to_me.setOnClickListener(this);
        ((Button) findViewById(R.id.feedback)).setOnClickListener(this);
        this.mHandler.sendEmptyMessage(MSG_INIT_DISPLAY_AD);
        this.settinglButton = (Button) findViewById(R.id.settinglButton);
        this.settinglButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent settingIntent = new Intent();
                settingIntent.setClass(MyLockScreen.this, RippleLockSetting.class);
                MyLockScreen.this.startActivity(settingIntent);
            }
        });
        this.contorlButton = (Button) findViewById(R.id.contorlButton);
        if (this.state == 1) {
            this.contorlButton.setText(getString(R.string.stop_lockscreen));
        } else {
            this.contorlButton.setText(getString(R.string.start_lockscreen));
        }
        final Intent service = new Intent(this, MyLockService.class);
        this.contorlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MyLockScreen.this.state == 1) {
                    MyLockScreen.this.stopService(service);
                    MyLockScreen.this.contorlButton.setText(MyLockScreen.this.getString(R.string.start_lockscreen));
                    MyLockScreen.this.state = 2;
                    MyLockScreen.this.makeToast(R.string.toast_stop_lockscreen);
                    return;
                }
                MyLockScreen.this.startService(service);
                MyLockScreen.this.contorlButton.setText(MyLockScreen.this.getString(R.string.stop_lockscreen));
                MyLockScreen.this.state = 1;
                MyLockScreen.this.makeToast(R.string.toast_start_lockscreen);
            }
        });
        showChangelog();
    }

    public void showChangelog() {
        if (this.util.isDisplayChangelog(42)) {
            if (!this.util.getValue(Util.KEY_SET_DOUBLE_MODE, false)) {
                this.util.saveFixDoubleLockIssue(3);
                this.util.saveValue(Util.KEY_SET_DOUBLE_MODE, true);
            }
            new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon).setTitle(R.string.changelog_title).setMessage(R.string.changelog).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).setNegativeButton(R.string.never_show, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MyLockScreen.this.util.saveDisplayChangelog(false, 42);
                }
            }).show();
        }
    }

    public void makeToast(int resId) {
        Toast.makeText(this, resId, 0).show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void savePref() {
        SharedPreferences.Editor editor = getSharedPreferences(MYLOCKSCREEN_PREF, 2).edit();
        editor.putInt(PREF_STATE, this.state);
        editor.commit();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        savePref();
    }

    public void onClick(View v) {
        if (v instanceof Button) {
            switch (((Button) v).getId()) {
                case R.id.OffersButton /*2131296288*/:
                    if (hasAd) {
                        Toast.makeText(this, R.string.ad_toast, 1).show();
                        MobclickAgent.onEvent(this, "offer_click");
                        return;
                    }
                    return;
                case R.id.email_to_me /*2131296303*/:
                    try {
                        startActivity(new Intent("android.intent.action.SENDTO", Uri.parse("mailto:ainanoha@163.com")));
                        return;
                    } catch (ActivityNotFoundException e) {
                        return;
                    }
                case R.id.feedback /*2131296304*/:
                    UMFeedbackService.openUmengFeedbackSDK(this);
                    return;
                case R.id.buy_app_button /*2131296307*/:
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=com.nanoha.MyLockScreen_all")));
                    return;
                default:
                    return;
            }
        }
    }

    public void getDisplayAdResponse(View adView2) {
        this.adView = adView2;
        adView2.setLayoutParams(new ViewGroup.LayoutParams(this.AdLinearLayout.getWidth(), this.AdLinearLayout.getHeight()));
        this.update_display_ad = true;
        this.mHandler.sendEmptyMessage(UPDATE_DISPLAYAD);
    }

    /* access modifiers changed from: private */
    public void updateResultsInUi() {
        if (this.update_display_ad) {
            this.AdLinearLayout.removeAllViews();
            this.AdLinearLayout.addView(this.adView);
            this.update_display_ad = false;
        }
    }

    public void getUpdatePoints(String currencyName, int pointTotal) {
        if (pointTotal >= 220) {
            hasAd = false;
            this.mHandler.sendEmptyMessage(REMOVE_AD);
            this.util.registFreeVersion();
            return;
        }
        this.update_text = true;
        this.displayText = String.valueOf(getString(R.string.current_point)) + ": " + pointTotal;
        this.mHandler.post(this.mUpdateResults);
    }

    public void getUpdatePointsFailed(String error) {
        this.update_text = true;
        this.displayText = error;
        this.mHandler.post(this.mUpdateResults);
    }
}
