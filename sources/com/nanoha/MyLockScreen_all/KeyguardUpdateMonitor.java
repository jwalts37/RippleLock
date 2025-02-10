package com.nanoha.MyLockScreen_all;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.mobclick.android.UmengConstants;
import com.nanoha.MyLockScreen_all.IccCard;
import com.nanoha.util.Util;
import java.util.ArrayList;

public class KeyguardUpdateMonitor {
    public static final String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    private static final boolean DEBUG = true;
    public static final String EXTRA_PLMN = "plmn";
    public static final String EXTRA_SHOW_PLMN = "showPlmn";
    public static final String EXTRA_SHOW_SPN = "showSpn";
    public static final String EXTRA_SPN = "spn";
    private static final int LOW_BATTERY_THRESHOLD = 20;
    private static final int MSG_BATTERY_UPDATE = 302;
    private static final int MSG_CARRIER_INFO_UPDATE = 303;
    private static final int MSG_PHONE_STATE_CHANGED = 306;
    private static final int MSG_SIM_STATE_CHANGE = 304;
    private static final int MSG_TIME_UPDATE = 301;
    public static final String SPN_STRINGS_UPDATED_ACTION = "android.provider.Telephony.SPN_STRINGS_UPDATED";
    private static final String TAG = "KeyguardUpdateMonitor";
    private BroadcastReceiver broadcaseReceiver;
    private final IntentFilter filter;
    private int mBatteryLevel;
    /* access modifiers changed from: private */
    public ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public final Context mContext;
    private boolean mDevicePluggedIn;
    /* access modifiers changed from: private */
    public boolean mDeviceProvisioned;
    private int mFailedAttempts = 0;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private ArrayList<InfoCallback> mInfoCallbacks = new ArrayList<>();
    private boolean mKeyguardBypassEnabled;
    private IccCard.State mSimState = IccCard.State.READY;
    private ArrayList<SimStateCallback> mSimStateCallbacks = new ArrayList<>();
    /* access modifiers changed from: private */
    public CharSequence mTelephonyPlmn;
    /* access modifiers changed from: private */
    public CharSequence mTelephonySpn;
    Util util;

    interface InfoCallback {
        void onPhoneStateChanged(String str);

        void onRefreshBatteryInfo(boolean z, boolean z2, int i);

        void onRefreshCarrierInfo(CharSequence charSequence, CharSequence charSequence2);

        void onTimeChanged();
    }

    interface SimStateCallback {
        void onSimStateChanged(IccCard.State state);
    }

    private static class SimArgs {
        public final IccCard.State simState;

        private SimArgs(Intent intent) {
            if (!KeyguardUpdateMonitor.ACTION_SIM_STATE_CHANGED.equals(intent.getAction())) {
                throw new IllegalArgumentException("only handles intent ACTION_SIM_STATE_CHANGED");
            }
            String stateExtra = intent.getStringExtra(IccCard.INTENT_KEY_ICC_STATE);
            if (IccCard.INTENT_VALUE_ICC_ABSENT.equals(stateExtra)) {
                this.simState = IccCard.State.ABSENT;
            } else if (IccCard.INTENT_VALUE_ICC_READY.equals(stateExtra)) {
                this.simState = IccCard.State.READY;
            } else if (IccCard.INTENT_VALUE_ICC_LOCKED.equals(stateExtra)) {
                String lockedReason = intent.getStringExtra(IccCard.INTENT_KEY_LOCKED_REASON);
                if (IccCard.INTENT_VALUE_LOCKED_ON_PIN.equals(lockedReason)) {
                    this.simState = IccCard.State.PIN_REQUIRED;
                } else if (IccCard.INTENT_VALUE_LOCKED_ON_PUK.equals(lockedReason)) {
                    this.simState = IccCard.State.PUK_REQUIRED;
                } else if (IccCard.INTENT_VALUE_LOCKED_NETWORK.equals(lockedReason)) {
                    this.simState = IccCard.State.NETWORK_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_NETWORK_SUBSET.equals(lockedReason)) {
                    this.simState = IccCard.State.SIM_NETWORK_SUBSET_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_CORPORATE.equals(lockedReason)) {
                    this.simState = IccCard.State.SIM_CORPORATE_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_SERVICE_PROVIDER.equals(lockedReason)) {
                    this.simState = IccCard.State.SIM_SERVICE_PROVIDER_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_SIM.equals(lockedReason)) {
                    this.simState = IccCard.State.SIM_SIM_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_NETWORK1.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_NETWORK1_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_NETWORK2.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_NETWORK2_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_HRPD.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_HRPD_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_CORPORATE.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_CORPORATE_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_SERVICE_PROVIDER.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_SERVICE_PROVIDER_LOCKED;
                } else if (IccCard.INTENT_VALUE_LOCKED_RUIM_RUIM.equals(lockedReason)) {
                    this.simState = IccCard.State.RUIM_RUIM_LOCKED;
                } else {
                    this.simState = IccCard.State.UNKNOWN;
                }
            } else if (IccCard.INTENT_VALUE_ICC_CARD_IO_ERROR.equals(stateExtra)) {
                this.simState = IccCard.State.CARD_IO_ERROR;
            } else {
                this.simState = IccCard.State.UNKNOWN;
            }
        }

        /* synthetic */ SimArgs(Intent intent, SimArgs simArgs) {
            this(intent);
        }

        public String toString() {
            return this.simState.toString();
        }
    }

    public KeyguardUpdateMonitor(Context context) {
        this.mContext = context;
        this.util = Util.getInstance(context);
        this.mHandler = new Handler() {
            /* Debug info: failed to restart local var, previous not found, register: 3 */
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case KeyguardUpdateMonitor.MSG_TIME_UPDATE /*301*/:
                        KeyguardUpdateMonitor.this.handleTimeUpdate();
                        return;
                    case KeyguardUpdateMonitor.MSG_BATTERY_UPDATE /*302*/:
                        KeyguardUpdateMonitor.this.handleBatteryUpdate(msg.arg1, msg.arg2);
                        return;
                    case KeyguardUpdateMonitor.MSG_CARRIER_INFO_UPDATE /*303*/:
                        KeyguardUpdateMonitor.this.handleCarrierInfoUpdate();
                        return;
                    case KeyguardUpdateMonitor.MSG_SIM_STATE_CHANGE /*304*/:
                        KeyguardUpdateMonitor.this.handleSimStateChange((SimArgs) msg.obj);
                        return;
                    case KeyguardUpdateMonitor.MSG_PHONE_STATE_CHANGED /*306*/:
                        KeyguardUpdateMonitor.this.handlePhoneStateChanged((String) msg.obj);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mKeyguardBypassEnabled = true;
        this.mDeviceProvisioned = Settings.Secure.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
        if (!this.mDeviceProvisioned) {
            this.mContentObserver = new ContentObserver(this.mHandler) {
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    KeyguardUpdateMonitor.this.mDeviceProvisioned = Settings.Secure.getInt(KeyguardUpdateMonitor.this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
                    if (KeyguardUpdateMonitor.this.mDeviceProvisioned && KeyguardUpdateMonitor.this.mContentObserver != null) {
                        KeyguardUpdateMonitor.this.mContext.getContentResolver().unregisterContentObserver(KeyguardUpdateMonitor.this.mContentObserver);
                        KeyguardUpdateMonitor.this.mContentObserver = null;
                    }
                    Log.d(KeyguardUpdateMonitor.TAG, "DEVICE_PROVISIONED state = " + KeyguardUpdateMonitor.this.mDeviceProvisioned);
                }
            };
            this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("device_provisioned"), false, this.mContentObserver);
            this.mDeviceProvisioned = Settings.Secure.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
        }
        this.mSimState = IccCard.State.READY;
        this.mDevicePluggedIn = true;
        this.mBatteryLevel = 100;
        this.mTelephonyPlmn = getDefaultPlmn();
        this.filter = new IntentFilter();
        this.filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.filter.addAction(ACTION_SIM_STATE_CHANGED);
        this.filter.addAction("android.intent.action.PHONE_STATE");
        this.filter.addAction("android.provider.Telephony.SPN_STRINGS_UPDATED");
        this.broadcaseReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(KeyguardUpdateMonitor.TAG, "received broadcast " + action);
                if ("android.provider.Telephony.SPN_STRINGS_UPDATED".equals(action)) {
                    KeyguardUpdateMonitor.this.mTelephonyPlmn = KeyguardUpdateMonitor.this.getTelephonyPlmnFrom(intent);
                    KeyguardUpdateMonitor.this.mTelephonySpn = KeyguardUpdateMonitor.this.getTelephonySpnFrom(intent);
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_CARRIER_INFO_UPDATE));
                } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_BATTERY_UPDATE, intent.getIntExtra("status", 1), intent.getIntExtra("level", 0)));
                } else if (KeyguardUpdateMonitor.ACTION_SIM_STATE_CHANGED.equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_SIM_STATE_CHANGE, new SimArgs(intent, (SimArgs) null)));
                } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_PHONE_STATE_CHANGED, intent.getStringExtra(UmengConstants.AtomKey_State)));
                }
            }
        };
    }

    public void onResume(Context context) {
        context.registerReceiver(this.broadcaseReceiver, this.filter);
    }

    public void onStop(Context context) {
    }

    public void onDestory() {
        if (this.broadcaseReceiver != null) {
            this.mContext.unregisterReceiver(this.broadcaseReceiver);
            this.broadcaseReceiver = null;
        }
    }

    /* access modifiers changed from: protected */
    public void handlePhoneStateChanged(String newState) {
        Log.d(TAG, "handlePhoneStateChanged(" + newState + ")");
        for (int i = 0; i < this.mInfoCallbacks.size(); i++) {
            this.mInfoCallbacks.get(i).onPhoneStateChanged(newState);
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeUpdate() {
        Log.d(TAG, "handleTimeUpdate");
        for (int i = 0; i < this.mInfoCallbacks.size(); i++) {
            this.mInfoCallbacks.get(i).onTimeChanged();
        }
    }

    /* access modifiers changed from: private */
    public void handleBatteryUpdate(int pluggedInStatus, int batteryLevel) {
        Log.d(TAG, "handleBatteryUpdate");
        boolean pluggedIn = isPluggedIn(pluggedInStatus);
        if (isBatteryUpdateInteresting(pluggedIn, batteryLevel)) {
            this.mBatteryLevel = batteryLevel;
            this.mDevicePluggedIn = pluggedIn;
            for (int i = 0; i < this.mInfoCallbacks.size(); i++) {
                this.mInfoCallbacks.get(i).onRefreshBatteryInfo(shouldShowBatteryInfo(), pluggedIn, batteryLevel);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleCarrierInfoUpdate() {
        Log.d(TAG, "handleCarrierInfoUpdate: plmn = " + this.mTelephonyPlmn + ", spn = " + this.mTelephonySpn);
        for (int i = 0; i < this.mInfoCallbacks.size(); i++) {
            this.mInfoCallbacks.get(i).onRefreshCarrierInfo(this.mTelephonyPlmn, this.mTelephonySpn);
        }
    }

    /* access modifiers changed from: private */
    public void handleSimStateChange(SimArgs simArgs) {
        IccCard.State state = simArgs.simState;
        Log.d(TAG, "handleSimStateChange: intentValue = " + simArgs + " " + "state resolved to " + state.toString());
        if (state != IccCard.State.UNKNOWN && state != this.mSimState) {
            this.mSimState = state;
            for (int i = 0; i < this.mSimStateCallbacks.size(); i++) {
                this.mSimStateCallbacks.get(i).onSimStateChanged(state);
            }
        }
    }

    private boolean isPluggedIn(int status) {
        return status == 2 || status == 5;
    }

    private boolean isBatteryUpdateInteresting(boolean pluggedIn, int batteryLevel) {
        if (this.mDevicePluggedIn != pluggedIn) {
            return true;
        }
        if (pluggedIn && this.mBatteryLevel != batteryLevel) {
            return true;
        }
        if (!pluggedIn && batteryLevel < 20 && batteryLevel != this.mBatteryLevel) {
            return true;
        }
        if (this.util.isDisplayBattery()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public CharSequence getTelephonyPlmnFrom(Intent intent) {
        if (!intent.getBooleanExtra("showPlmn", false)) {
            return null;
        }
        String plmn = intent.getStringExtra("plmn");
        if (plmn != null) {
            return plmn;
        }
        return getDefaultPlmn();
    }

    private CharSequence getDefaultPlmn() {
        return this.mContext.getResources().getText(R.string.lockscreen_carrier_default);
    }

    /* access modifiers changed from: private */
    public CharSequence getTelephonySpnFrom(Intent intent) {
        String spn;
        if (!intent.getBooleanExtra("showSpn", false) || (spn = intent.getStringExtra("spn")) == null) {
            return null;
        }
        return spn;
    }

    public void removeCallback(Object observer) {
        this.mInfoCallbacks.remove(observer);
        this.mSimStateCallbacks.remove(observer);
    }

    public void registerInfoCallback(InfoCallback callback) {
        if (!this.mInfoCallbacks.contains(callback)) {
            this.mInfoCallbacks.add(callback);
        } else {
            Log.e(TAG, "Object tried to add another INFO callback", new Exception("Whoops"));
        }
    }

    public void registerSimStateCallback(SimStateCallback callback) {
        if (!this.mSimStateCallbacks.contains(callback)) {
            this.mSimStateCallbacks.add(callback);
        } else {
            Log.e(TAG, "Object tried to add another SIM callback", new Exception("Whoops"));
        }
    }

    public IccCard.State getSimState() {
        return this.mSimState;
    }

    public void reportSimPinUnlocked() {
        this.mSimState = IccCard.State.READY;
    }

    public boolean isKeyguardBypassEnabled() {
        return this.mKeyguardBypassEnabled;
    }

    public boolean isDevicePluggedIn() {
        return this.mDevicePluggedIn;
    }

    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }

    public boolean shouldShowBatteryInfo() {
        return this.mDevicePluggedIn || this.mBatteryLevel < 20;
    }

    public CharSequence getTelephonyPlmn() {
        return this.mTelephonyPlmn;
    }

    public CharSequence getTelephonySpn() {
        return this.mTelephonySpn;
    }

    public boolean isDeviceProvisioned() {
        return this.mDeviceProvisioned;
    }

    public int getFailedAttempts() {
        return this.mFailedAttempts;
    }

    public void clearFailedAttempts() {
        this.mFailedAttempts = 0;
    }

    public void reportFailedAttempt() {
        this.mFailedAttempts++;
    }
}
