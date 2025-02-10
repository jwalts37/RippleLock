package com.nanoha.MyLockScreen_all;

import android.content.res.Configuration;

public interface KeyguardScreenCallback extends KeyguardViewCallback {
    boolean doesFallbackUnlockScreenExist();

    void forgotPattern(boolean z);

    void goToLockScreen();

    void goToUnlockScreen();

    boolean isSecure();

    boolean isVerifyUnlockOnly();

    void recreateMe(Configuration configuration);

    void reportFailedUnlockAttempt();

    void reportSuccessfulUnlockAttempt();

    void takeEmergencyCallAction();
}
