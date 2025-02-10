package com.nanoha.MyLockScreen_all;

public interface KeyguardViewCallback {
    void keyguardDone(boolean z);

    void keyguardDoneDrawing();

    void pokeWakelock();

    void pokeWakelock(int i);
}
