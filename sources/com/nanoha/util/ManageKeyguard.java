package com.nanoha.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;

public class ManageKeyguard {
    private static final boolean DEBUG = true;
    private static final String LOGTAG = "LOCKSCREEN_KEYGUARDMANAGER";
    private static KeyguardManager.KeyguardLock myKL = null;
    private static KeyguardManager myKM = null;

    public interface LaunchOnKeyguardExit {
        void LaunchOnKeyguardExitSuccess();
    }

    public static synchronized void initialize(Context context) {
        synchronized (ManageKeyguard.class) {
            if (myKM == null) {
                myKM = (KeyguardManager) context.getSystemService("keyguard");
            }
        }
    }

    public static synchronized void alwaysDisableKeyguard(Context context) {
        synchronized (ManageKeyguard.class) {
            initialize(context);
            myKL = myKM.newKeyguardLock("nanohaKeyguard");
            myKL.disableKeyguard();
            Log.v(LOGTAG, "--Keyguard disabled");
        }
    }

    public static synchronized void alwaysExitKeyguardSecurely(final LaunchOnKeyguardExit callback) {
        synchronized (ManageKeyguard.class) {
            Log.v(LOGTAG, "--Trying to exit keyguard securely");
            myKM.exitKeyguardSecurely(new KeyguardManager.OnKeyguardExitResult() {
                public void onKeyguardExitResult(boolean success) {
                    ManageKeyguard.reenableKeyguard();
                    if (success) {
                        Log.v(ManageKeyguard.LOGTAG, "--Keyguard exited securely");
                        if (LaunchOnKeyguardExit.this != null) {
                            LaunchOnKeyguardExit.this.LaunchOnKeyguardExitSuccess();
                            return;
                        }
                        return;
                    }
                    Log.v(ManageKeyguard.LOGTAG, "--Keyguard exit failed");
                }
            });
        }
    }

    public static synchronized void disableKeyguard(Context context) {
        synchronized (ManageKeyguard.class) {
            initialize(context);
            if (myKM.inKeyguardRestrictedInputMode()) {
                myKL = myKM.newKeyguardLock("nanohaKeyguard");
                myKL.disableKeyguard();
                Log.v(LOGTAG, "--Keyguard disabled");
            } else {
                Log.v(LOGTAG, "--Keyguard not disabled....");
                myKL = null;
            }
        }
    }

    public static synchronized boolean inKeyguardRestrictedInputMode() {
        boolean z;
        synchronized (ManageKeyguard.class) {
            if (myKM != null) {
                Log.v(LOGTAG, "--inKeyguardRestrictedInputMode = " + myKM.inKeyguardRestrictedInputMode());
                z = myKM.inKeyguardRestrictedInputMode();
            } else {
                z = false;
            }
        }
        return z;
    }

    public static synchronized void reenableKeyguard() {
        synchronized (ManageKeyguard.class) {
            if (!(myKM == null || myKL == null)) {
                myKL.reenableKeyguard();
                myKL = null;
                Log.v(LOGTAG, "--Keyguard reenabled");
            }
        }
    }

    public static synchronized void exitKeyguardSecurely(final LaunchOnKeyguardExit callback) {
        synchronized (ManageKeyguard.class) {
            if (inKeyguardRestrictedInputMode()) {
                Log.v(LOGTAG, "--Trying to exit keyguard securely");
                myKM.exitKeyguardSecurely(new KeyguardManager.OnKeyguardExitResult() {
                    public void onKeyguardExitResult(boolean success) {
                        ManageKeyguard.reenableKeyguard();
                        if (success) {
                            Log.v(ManageKeyguard.LOGTAG, "--Keyguard exited securely");
                            if (LaunchOnKeyguardExit.this != null) {
                                LaunchOnKeyguardExit.this.LaunchOnKeyguardExitSuccess();
                                return;
                            }
                            return;
                        }
                        Log.v(ManageKeyguard.LOGTAG, "--Keyguard exit failed");
                    }
                });
            } else if (callback != null) {
                callback.LaunchOnKeyguardExitSuccess();
            }
        }
    }
}
