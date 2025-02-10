package com.nanoha.MyLockScreen_all;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.nanoha.util.ManageKeyguard;
import com.nanoha.util.Util;

public class ScreenReceiver extends BroadcastReceiver {
    private static final int MSG_SCREEN_OFF = 2011;
    private static final int MSG_SCREEN_ON = 2012;
    private static final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    private static final String SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String TAG = "ScreenReceiver";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Context context = (Context) msg.obj;
            Util util = Util.getInstance(context);
            int fixInt = util.getFixDoubleLockIssue();
            boolean useSecurityLock = util.isUseSecurityLock();
            switch (msg.what) {
                case ScreenReceiver.MSG_SCREEN_OFF /*2011*/:
                    if (((TelephonyManager) context.getSystemService("phone")).getCallState() == 0) {
                        Intent circleIntent = new Intent();
                        circleIntent.addFlags(268435456);
                        circleIntent.addFlags(65536);
                        circleIntent.setClass(context, CircleLockScreen.class);
                        context.startActivity(circleIntent);
                        if (fixInt == 3) {
                            ManageKeyguard.reenableKeyguard();
                            return;
                        } else if (!useSecurityLock && fixInt == 0) {
                            ManageKeyguard.exitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case ScreenReceiver.MSG_SCREEN_ON /*2012*/:
                    com.nanoha.smartClock.Util.getInstance(context).updateClockWidget();
                    if (fixInt == 3) {
                        if (util.isUseCallButton() || !ScreenReceiver.this.inCall(context)) {
                            ManageKeyguard.alwaysDisableKeyguard(context);
                            return;
                        }
                        return;
                    } else if (!useSecurityLock && fixInt == 0 && !ScreenReceiver.this.inCall(context)) {
                        ManageKeyguard.disableKeyguard(context);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Util util = Util.getInstance(context);
        int delayLock = util.getDelayLock();
        boolean isUseSecurity = util.isUseSecurityLock();
        if (SCREEN_OFF.equals(intent.getAction())) {
            if (delayLock == 0) {
                this.mHandler.obtainMessage(MSG_SCREEN_OFF, context).sendToTarget();
                return;
            }
            if (isUseSecurity) {
                ManageKeyguard.alwaysDisableKeyguard(context);
            }
            this.mHandler.removeMessages(MSG_SCREEN_OFF, context);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MSG_SCREEN_OFF, context), (long) delayLock);
        } else if (!SCREEN_ON.equals(action)) {
        } else {
            if (delayLock == 0) {
                this.mHandler.obtainMessage(MSG_SCREEN_ON, context).sendToTarget();
            } else if (this.mHandler.hasMessages(MSG_SCREEN_OFF, context)) {
                this.mHandler.removeMessages(MSG_SCREEN_OFF, context);
                if (isUseSecurity) {
                    ManageKeyguard.alwaysExitKeyguardSecurely((ManageKeyguard.LaunchOnKeyguardExit) null);
                }
            } else {
                this.mHandler.obtainMessage(MSG_SCREEN_ON, context).sendToTarget();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean inCall(Context context) {
        switch (((TelephonyManager) context.getSystemService("phone")).getCallState()) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            default:
                return false;
        }
    }
}
