package com.nanoha.MyLockScreen_all;

import android.util.Log;

public abstract class IccCard {
    public static final String INTENT_KEY_ICC_STATE = "ss";
    public static final String INTENT_KEY_LOCKED_REASON = "reason";
    public static final String INTENT_VALUE_ICC_ABSENT = "ABSENT";
    public static final String INTENT_VALUE_ICC_CARD_IO_ERROR = "CARD_IO_ERROR";
    public static final String INTENT_VALUE_ICC_IMSI = "IMSI";
    public static final String INTENT_VALUE_ICC_LOADED = "LOADED";
    public static final String INTENT_VALUE_ICC_LOCKED = "LOCKED";
    public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
    public static final String INTENT_VALUE_ICC_READY = "READY";
    public static final String INTENT_VALUE_LOCKED_CORPORATE = "SIM CORPORATE";
    public static final String INTENT_VALUE_LOCKED_NETWORK = "SIM NETWORK";
    public static final String INTENT_VALUE_LOCKED_NETWORK_SUBSET = "SIM NETWORK SUBSET";
    public static final String INTENT_VALUE_LOCKED_ON_PIN = "PIN";
    public static final String INTENT_VALUE_LOCKED_ON_PUK = "PUK";
    public static final String INTENT_VALUE_LOCKED_RUIM_CORPORATE = "RUIM CORPORATE";
    public static final String INTENT_VALUE_LOCKED_RUIM_HRPD = "RUIM HRPD";
    public static final String INTENT_VALUE_LOCKED_RUIM_NETWORK1 = "RUIM NETWORK1";
    public static final String INTENT_VALUE_LOCKED_RUIM_NETWORK2 = "RUIM NETWORK2";
    public static final String INTENT_VALUE_LOCKED_RUIM_RUIM = "RUIM RUIM";
    public static final String INTENT_VALUE_LOCKED_RUIM_SERVICE_PROVIDER = "RUIM SERVICE PROVIDER";
    public static final String INTENT_VALUE_LOCKED_SERVICE_PROVIDER = "SIM SERVICE PROVIDER";
    public static final String INTENT_VALUE_LOCKED_SIM = "SIM SIM";
    protected boolean mDbg;
    private boolean mIccFdnAvailable = true;
    private boolean mIccFdnEnabled = false;
    private boolean mIccPinLocked = true;
    protected String mLogTag;
    private int mPin1RetryCount = -1;
    private int mPin2RetryCount = -1;
    protected State mState = null;

    public abstract void dispose();

    public enum State {
        UNKNOWN,
        ABSENT,
        PIN_REQUIRED,
        PUK_REQUIRED,
        NETWORK_LOCKED,
        READY,
        CARD_IO_ERROR,
        SIM_NETWORK_SUBSET_LOCKED,
        SIM_CORPORATE_LOCKED,
        SIM_SERVICE_PROVIDER_LOCKED,
        SIM_SIM_LOCKED,
        RUIM_NETWORK1_LOCKED,
        RUIM_NETWORK2_LOCKED,
        RUIM_HRPD_LOCKED,
        RUIM_CORPORATE_LOCKED,
        RUIM_SERVICE_PROVIDER_LOCKED,
        RUIM_RUIM_LOCKED,
        NOT_READY;

        public boolean isPinLocked() {
            return this == PIN_REQUIRED || this == PUK_REQUIRED;
        }
    }

    public State getState() {
        if (this.mState != null) {
            return this.mState;
        }
        Log.e(this.mLogTag, "IccCard.getState(): case should never be reached");
        return State.UNKNOWN;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (this.mDbg) {
            Log.d(this.mLogTag, "IccCard finalized");
        }
    }

    public boolean getIccFdnAvailable() {
        return this.mIccFdnAvailable;
    }

    public boolean getIccLockEnabled() {
        return this.mIccPinLocked;
    }

    public boolean getIccFdnEnabled() {
        return this.mIccFdnEnabled;
    }

    public int getIccPin1RetryCount() {
        return this.mPin1RetryCount;
    }

    public int getIccPin2RetryCount() {
        return this.mPin2RetryCount;
    }
}
