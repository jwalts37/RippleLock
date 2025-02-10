package com.nanoha.MyLockScreen_all;

public interface KeyguardScreen {
    void cleanUp();

    boolean needsInput();

    void onPause();

    void onResume();
}
