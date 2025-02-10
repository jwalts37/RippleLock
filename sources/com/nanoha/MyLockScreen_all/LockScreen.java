package com.nanoha.MyLockScreen_all;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nanoha.CropImage.CropImageUtil;
import com.nanoha.MyLockScreen_all.IccCard;
import com.nanoha.MyLockScreen_all.KeyguardUpdateMonitor;
import com.nanoha.circle.NewScreen;
import com.nanoha.util.Util;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class LockScreen extends LinearLayout implements KeyguardScreen, KeyguardUpdateMonitor.InfoCallback, KeyguardUpdateMonitor.SimStateCallback {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nanoha$MyLockScreen_all$IccCard$State = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nanoha$MyLockScreen_all$LockScreen$Status = null;
    static final String ACTION_EMERGENCY_DIAL = "com.android.phone.EmergencyDialer.DIAL";
    private static final boolean DBG = false;
    private static final String TAG = "LockScreen";
    private TextView am_pm;
    private List<Bitmap> bitmapCache = null;
    boolean isDisplayStatus = true;
    boolean isOnlyDisplayLockCircle = DBG;
    private Drawable mAlarmIcon = null;
    private int mBatteryLevel = 100;
    private TextView mCarrier;
    private String mCharging = null;
    private Drawable mChargingIcon = null;
    private Context mContext;
    private int mCreationOrientation;
    private TextView mDate;
    private String mDateFormatString;
    private Button mEmergencyCallButton;
    private TextView mEmergencyCallText;
    private Typeface mFace;
    private IccText mIccText;
    private String mNextAlarm = null;
    private boolean mPluggedIn = DBG;
    private IccText mRuimText;
    private TextView mScreenLocked;
    private boolean mShowingBatteryInfo = DBG;
    private IccText mSimText;
    private Status mStatus = Status.Normal;
    private TextView mStatus1;
    private TextView mStatus2;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private NewScreen newScreen;
    private TextView timeDisplay;
    private Util util;

    static /* synthetic */ int[] $SWITCH_TABLE$com$nanoha$MyLockScreen_all$IccCard$State() {
        int[] iArr = $SWITCH_TABLE$com$nanoha$MyLockScreen_all$IccCard$State;
        if (iArr == null) {
            iArr = new int[IccCard.State.values().length];
            try {
                iArr[IccCard.State.ABSENT.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[IccCard.State.CARD_IO_ERROR.ordinal()] = 7;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[IccCard.State.NETWORK_LOCKED.ordinal()] = 5;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[IccCard.State.NOT_READY.ordinal()] = 18;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[IccCard.State.PIN_REQUIRED.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[IccCard.State.PUK_REQUIRED.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[IccCard.State.READY.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[IccCard.State.RUIM_CORPORATE_LOCKED.ordinal()] = 15;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[IccCard.State.RUIM_HRPD_LOCKED.ordinal()] = 14;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[IccCard.State.RUIM_NETWORK1_LOCKED.ordinal()] = 12;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[IccCard.State.RUIM_NETWORK2_LOCKED.ordinal()] = 13;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[IccCard.State.RUIM_RUIM_LOCKED.ordinal()] = 17;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[IccCard.State.RUIM_SERVICE_PROVIDER_LOCKED.ordinal()] = 16;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[IccCard.State.SIM_CORPORATE_LOCKED.ordinal()] = 9;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[IccCard.State.SIM_NETWORK_SUBSET_LOCKED.ordinal()] = 8;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[IccCard.State.SIM_SERVICE_PROVIDER_LOCKED.ordinal()] = 10;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[IccCard.State.SIM_SIM_LOCKED.ordinal()] = 11;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[IccCard.State.UNKNOWN.ordinal()] = 1;
            } catch (NoSuchFieldError e18) {
            }
            $SWITCH_TABLE$com$nanoha$MyLockScreen_all$IccCard$State = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$nanoha$MyLockScreen_all$LockScreen$Status() {
        int[] iArr = $SWITCH_TABLE$com$nanoha$MyLockScreen_all$LockScreen$Status;
        if (iArr == null) {
            iArr = new int[Status.values().length];
            try {
                iArr[Status.CorporateLocked.ordinal()] = 9;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Status.NetworkLocked.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Status.NetworkSubsetLocked.ordinal()] = 8;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Status.Normal.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Status.RuimCorporateLocked.ordinal()] = 15;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Status.RuimHrpdLocked.ordinal()] = 14;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Status.RuimNetwork1Locked.ordinal()] = 12;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[Status.RuimNetwork2Locked.ordinal()] = 13;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[Status.RuimRuimLocked.ordinal()] = 17;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[Status.RuimServiceProviderLocked.ordinal()] = 16;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[Status.ServiceProviderLocked.ordinal()] = 10;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[Status.SimIOError.ordinal()] = 7;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[Status.SimLocked.ordinal()] = 6;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[Status.SimMissing.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[Status.SimMissingLocked.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[Status.SimPukLocked.ordinal()] = 5;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[Status.SimSimLocked.ordinal()] = 11;
            } catch (NoSuchFieldError e17) {
            }
            $SWITCH_TABLE$com$nanoha$MyLockScreen_all$LockScreen$Status = iArr;
        }
        return iArr;
    }

    enum Status {
        Normal(true),
        NetworkLocked(true),
        SimMissing(LockScreen.DBG),
        SimMissingLocked(LockScreen.DBG),
        SimPukLocked(LockScreen.DBG),
        SimLocked(true),
        SimIOError(true),
        NetworkSubsetLocked(true),
        CorporateLocked(true),
        ServiceProviderLocked(true),
        SimSimLocked(true),
        RuimNetwork1Locked(true),
        RuimNetwork2Locked(true),
        RuimHrpdLocked(true),
        RuimCorporateLocked(true),
        RuimServiceProviderLocked(true),
        RuimRuimLocked(true);
        
        private final boolean mShowStatusLines;

        private Status(boolean mShowStatusLines2) {
            this.mShowStatusLines = mShowStatusLines2;
        }

        public boolean showStatusLines() {
            return this.mShowStatusLines;
        }
    }

    public void takeEmergencyCallAction() {
        Intent intent = new Intent(ACTION_EMERGENCY_DIAL);
        intent.setFlags(276824064);
        getContext().startActivity(intent);
    }

    LockScreen(Context context, Configuration configuration, KeyguardUpdateMonitor updateMonitor, boolean isSelectWidget) {
        super(context);
        this.mContext = context;
        this.util = Util.getInstance(context);
        this.mUpdateMonitor = updateMonitor;
        this.mCreationOrientation = configuration.orientation;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (isSelectWidget) {
            if (this.mCreationOrientation != 2) {
                inflater.inflate(R.layout.circle_unlock_widget, this, true);
            } else {
                inflater.inflate(R.layout.circle_unlock_land_widget, this, true);
            }
        } else if (this.mCreationOrientation != 2) {
            inflater.inflate(R.layout.circle_unlock, this, true);
        } else {
            inflater.inflate(R.layout.circle_unlock_land, this, true);
        }
        int alpha = this.util.getBackgroundAlpha();
        alpha = alpha > 200 ? 200 : alpha;
        this.newScreen = (NewScreen) findViewById(R.id.circleScreen);
        ImageView screenBackground = (ImageView) findViewById(R.id.screenBackground);
        screenBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String backgroundUri = this.util.getBackground();
        if (!"0".equals(backgroundUri)) {
            try {
                Bitmap bitmap = readBitMap(context.getContentResolver(), backgroundUri);
                this.bitmapCache = new ArrayList();
                this.bitmapCache.add(bitmap);
                screenBackground.setImageBitmap(bitmap);
            } catch (Exception e) {
                Exception e2 = e;
                Log.e("Exception", e2.getMessage(), e2);
                screenBackground.setBackgroundDrawable(context.getWallpaper());
            } catch (OutOfMemoryError e3) {
                OutOfMemoryError e4 = e3;
                Log.e("Error", e4.getMessage(), e4);
                screenBackground.setBackgroundDrawable(context.getWallpaper());
            }
            screenBackground.setAlpha(255 - alpha);
        } else {
            setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        }
        this.mCarrier = (TextView) findViewById(R.id.carrier);
        this.mCarrier.setSelected(true);
        this.mCarrier.setTextColor(-1);
        if (this.util.isDisplayCarrier()) {
            this.mCarrier.setVisibility(0);
        } else {
            this.mCarrier.setVisibility(4);
        }
        this.mDate = (TextView) findViewById(R.id.date);
        this.mStatus1 = (TextView) findViewById(R.id.status1);
        this.mStatus2 = (TextView) findViewById(R.id.status2);
        this.timeDisplay = (TextView) findViewById(R.id.timeDisplay);
        this.am_pm = (TextView) findViewById(R.id.am_pm);
        this.mScreenLocked = (TextView) findViewById(R.id.screenLocked);
        this.mEmergencyCallText = (TextView) findViewById(R.id.emergencyCallText);
        this.mEmergencyCallButton = (Button) findViewById(R.id.emergencyCallButton);
        this.mEmergencyCallButton.setText(R.string.lockscreen_emergency_call);
        updateEmergencyCallButtonState(this.mEmergencyCallButton);
        this.mEmergencyCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LockScreen.this.takeEmergencyCallAction();
            }
        });
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(393216);
        updateMonitor.registerInfoCallback(this);
        updateMonitor.registerSimStateCallback(this);
        resetStatusInfo(updateMonitor);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        initTextSize(dm.widthPixels, dm.heightPixels);
        initTextColor();
        initTextFont();
        initTextVisible();
    }

    private void initTextVisible() {
        this.isOnlyDisplayLockCircle = this.util.isOnlyDisplayLockCircle();
        if (this.isOnlyDisplayLockCircle) {
            invisibleAll();
            return;
        }
        if (!this.util.isDisplayCarrier()) {
            this.mCarrier.setVisibility(4);
        }
        if (!this.util.isDisplayTime()) {
            this.timeDisplay.setVisibility(4);
            this.am_pm.setVisibility(4);
        }
        if (!this.util.isDisplayDate()) {
            this.mDate.setVisibility(4);
        }
        this.isDisplayStatus = this.util.isDisplayStatus();
        if (!this.isDisplayStatus) {
            this.mStatus1.setVisibility(4);
            this.mStatus2.setVisibility(4);
        }
    }

    public void ringingCall(String number) {
        if (this.newScreen != null) {
            this.newScreen.ringingCall(number);
        }
    }

    public void idealCall() {
        if (this.newScreen != null) {
            this.newScreen.idealCall();
        }
    }

    public Bitmap readBitMap(ContentResolver cr, String uri) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(uri)), (Rect) null, opt);
    }

    public void initTextColor() {
        if (this.util.isColorAuto()) {
            this.timeDisplay.setTextColor(-1);
            this.am_pm.setTextColor(-1);
            this.mDate.setTextColor(-1);
            this.mCarrier.setTextColor(-1);
            this.mStatus1.setTextColor(-1);
            this.mStatus2.setTextColor(-1);
            return;
        }
        this.timeDisplay.setTextColor(this.util.getTimeColor());
        this.am_pm.setTextColor(this.util.getAmColor());
        this.mDate.setTextColor(this.util.getDateColor());
        this.mCarrier.setTextColor(this.util.getCarrierColor());
        this.mStatus1.setTextColor(this.util.getStatusColor());
        this.mStatus2.setTextColor(this.util.getStatusColor());
    }

    public void invisibleAll() {
        this.timeDisplay.setVisibility(4);
        this.am_pm.setVisibility(4);
        this.mDate.setVisibility(4);
        this.mCarrier.setVisibility(4);
        this.mStatus1.setVisibility(4);
        this.mStatus2.setVisibility(4);
    }

    public void initTextFont() {
        int fontIndex = this.util.getFontStyle();
        if (fontIndex == 0) {
            return;
        }
        if (5 == fontIndex) {
            try {
                String fontPath = this.util.getCustomFontPath();
                if (fontPath != null) {
                    try {
                        this.mFace = Typeface.createFromFile(fontPath);
                        this.timeDisplay.setTypeface(this.mFace);
                        this.am_pm.setTypeface(this.mFace);
                        this.mDate.setTypeface(this.mFace);
                        this.mStatus1.setTypeface(this.mFace);
                        this.mStatus2.setTypeface(this.mFace);
                        this.mScreenLocked.setTypeface(this.mFace);
                        this.mCarrier.setTypeface(this.mFace);
                        this.mEmergencyCallText.setTypeface(this.mFace);
                    } catch (Exception e) {
                        Log.e("testABC", "Does not support this font:" + e.toString());
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (4 == fontIndex) {
            this.mFace = Typeface.createFromAsset(getContext().getAssets(), "Clockopia.ttf");
            this.timeDisplay.setTypeface(this.mFace);
            this.am_pm.setTypeface(this.mFace);
        } else {
            this.mFace = Typeface.createFromAsset(getContext().getAssets(), String.valueOf(fontIndex) + ".ttf");
            this.timeDisplay.setTypeface(this.mFace);
            this.am_pm.setTypeface(this.mFace);
            this.mDate.setTypeface(this.mFace);
            this.mStatus1.setTypeface(this.mFace);
            this.mStatus2.setTypeface(this.mFace);
            this.mScreenLocked.setTypeface(this.mFace);
            this.mCarrier.setTypeface(this.mFace);
            this.mEmergencyCallText.setTypeface(this.mFace);
        }
    }

    public void initTextSize(int width, int height) {
        int min = Math.min(width, height);
        float smallSize = (float) (min / 14);
        float mediumSize = (float) (min / 12);
        float bigSize = (float) (min / 5);
        if (smallSize > 28.0f) {
            smallSize = 28.0f;
        }
        if (mediumSize > 30.0f) {
            mediumSize = 30.0f;
        }
        if (bigSize > 72.0f) {
            bigSize = 72.0f;
        }
        if (this.util.isSizeAuto()) {
            this.timeDisplay.setTextSize(bigSize);
            this.am_pm.setTextSize(mediumSize);
            this.mDate.setTextSize(mediumSize);
            this.mStatus1.setTextSize(mediumSize);
            this.mStatus2.setTextSize(mediumSize);
            this.mScreenLocked.setTextSize(mediumSize);
            this.mCarrier.setTextSize(smallSize);
            this.mEmergencyCallText.setTextSize(smallSize);
            return;
        }
        int timeSize = this.util.getTimeSize();
        int amSize = this.util.getAmSize();
        int dateSize = this.util.getDateSize();
        int statusSize = this.util.getStatusSize();
        int carrierSize = this.util.getCarrierSize();
        if (timeSize == 0) {
            this.timeDisplay.setTextSize(bigSize);
        } else {
            this.timeDisplay.setTextSize((float) timeSize);
        }
        if (amSize == 0) {
            this.am_pm.setTextSize(mediumSize);
        } else {
            this.am_pm.setTextSize((float) amSize);
        }
        if (dateSize == 0) {
            this.mDate.setTextSize(mediumSize);
        } else {
            this.mDate.setTextSize((float) dateSize);
        }
        if (statusSize == 0) {
            this.mStatus1.setTextSize(mediumSize);
            this.mStatus2.setTextSize(mediumSize);
            this.mScreenLocked.setTextSize(mediumSize);
        } else {
            this.mStatus1.setTextSize((float) statusSize);
            this.mStatus2.setTextSize((float) statusSize);
            this.mScreenLocked.setTextSize((float) statusSize);
        }
        if (carrierSize == 0) {
            this.mCarrier.setTextSize(smallSize);
            this.mEmergencyCallText.setTextSize(smallSize);
            return;
        }
        this.mCarrier.setTextSize((float) carrierSize);
        this.mEmergencyCallText.setTextSize((float) carrierSize);
    }

    public void onDestory() {
        if (this.newScreen != null) {
            this.newScreen.onDestory();
        }
        if (this.bitmapCache != null && this.bitmapCache.size() > 0) {
            for (int i = 0; i < this.bitmapCache.size(); i++) {
                Bitmap bitmap = this.bitmapCache.get(i);
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
        this.mUpdateMonitor.onDestory();
        System.gc();
    }

    public void updateEmergencyCallButtonState(Button button) {
        int textId;
        if (((TelephonyManager) this.mContext.getSystemService("phone")).getCallState() == 2) {
            textId = R.string.lockscreen_return_to_call;
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stat_sys_phone_call, 0, 0, 0);
        } else {
            textId = R.string.lockscreen_emergency_call;
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_emergency, 0, 0, 0);
        }
        button.setText(textId);
    }

    private void resetStatusInfo(KeyguardUpdateMonitor updateMonitor) {
        this.mShowingBatteryInfo = updateMonitor.shouldShowBatteryInfo();
        this.mPluggedIn = updateMonitor.isDevicePluggedIn();
        this.mBatteryLevel = updateMonitor.getBatteryLevel();
        this.mStatus = getCurrentStatus(updateMonitor.getSimState());
        updateLayout(this.mStatus);
        refreshBatteryStringAndIcon();
        refreshAlarmDisplay();
        String dateFormat = this.util.getDateformat();
        if ("0".equals(dateFormat)) {
            this.mDateFormatString = getContext().getString(R.string.full_wday_month_day_no_year);
        } else {
            this.mDateFormatString = dateFormat;
        }
        refreshTimeAndDateDisplay();
        updateStatusLines();
    }

    private void refreshAlarmDisplay() {
        this.mNextAlarm = getNextAlarm();
        if (this.mNextAlarm != null) {
            this.mAlarmIcon = getContext().getResources().getDrawable(R.drawable.ic_lock_idle_alarm);
        }
        updateStatusLines();
    }

    public String getNextAlarm() {
        String nextAlarm = Settings.System.getString(this.mContext.getContentResolver(), "next_alarm_formatted");
        if (nextAlarm == null || TextUtils.isEmpty(nextAlarm)) {
            return null;
        }
        return nextAlarm;
    }

    public void onRefreshBatteryInfo(boolean showBatteryInfo, boolean pluggedIn, int batteryLevel) {
        this.mShowingBatteryInfo = showBatteryInfo;
        this.mPluggedIn = pluggedIn;
        this.mBatteryLevel = batteryLevel;
        refreshBatteryStringAndIcon();
        updateStatusLines();
    }

    private void refreshBatteryStringAndIcon() {
        if (this.mShowingBatteryInfo) {
            if (this.mChargingIcon == null) {
                this.mChargingIcon = getContext().getResources().getDrawable(R.drawable.ic_lock_idle_charging);
            }
            if (!this.mPluggedIn) {
                this.mCharging = getContext().getString(R.string.lockscreen_low_battery);
            } else if (this.mBatteryLevel >= 100) {
                this.mCharging = getContext().getString(R.string.lockscreen_charged);
            } else {
                this.mCharging = getContext().getString(R.string.lockscreen_plugged_in, new Object[]{Integer.valueOf(this.mBatteryLevel)});
            }
        } else if (this.util.isDisplayBattery()) {
            if (this.mChargingIcon == null) {
                this.mChargingIcon = getContext().getResources().getDrawable(R.drawable.ic_lock_idle_charging);
            }
            if (this.mBatteryLevel > 100) {
                this.mBatteryLevel = 100;
            }
            this.mCharging = getContext().getString(R.string.lockscreen_current_battery, new Object[]{Integer.valueOf(this.mBatteryLevel)});
        } else {
            this.mCharging = null;
        }
    }

    public void onTimeChanged() {
        refreshTimeAndDateDisplay();
    }

    private void refreshTimeAndDateDisplay() {
        this.mDate.setText(DateFormat.format(this.mDateFormatString, new Date()));
    }

    private void updateStatusLines() {
        if (!this.isOnlyDisplayLockCircle && this.isDisplayStatus) {
            if (!this.mStatus.showStatusLines() || (this.mCharging == null && this.mNextAlarm == null)) {
                this.mStatus1.setVisibility(4);
                this.mStatus2.setVisibility(4);
            } else if (this.mCharging != null && this.mNextAlarm == null) {
                this.mStatus1.setVisibility(0);
                this.mStatus2.setVisibility(4);
                this.mStatus1.setText(this.mCharging);
                this.mStatus1.setCompoundDrawablesWithIntrinsicBounds(this.mChargingIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            } else if (this.mNextAlarm != null && this.mCharging == null) {
                this.mStatus1.setVisibility(0);
                this.mStatus2.setVisibility(4);
                this.mStatus1.setText(this.mNextAlarm);
                this.mStatus1.setCompoundDrawablesWithIntrinsicBounds(this.mAlarmIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            } else if (this.mCharging != null && this.mNextAlarm != null) {
                this.mStatus1.setVisibility(0);
                this.mStatus2.setVisibility(0);
                this.mStatus1.setText(this.mCharging);
                this.mStatus1.setCompoundDrawablesWithIntrinsicBounds(this.mChargingIcon, (Drawable) null, (Drawable) null, (Drawable) null);
                this.mStatus2.setText(this.mNextAlarm);
                this.mStatus2.setCompoundDrawablesWithIntrinsicBounds(this.mAlarmIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }
        }
    }

    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn) {
        updateLayout(this.mStatus);
    }

    private Status getCurrentStatus(IccCard.State simState) {
        if (!this.mUpdateMonitor.isDeviceProvisioned() && simState == IccCard.State.ABSENT) {
            return Status.SimMissingLocked;
        }
        switch ($SWITCH_TABLE$com$nanoha$MyLockScreen_all$IccCard$State()[simState.ordinal()]) {
            case 1:
                return Status.SimMissing;
            case 2:
                return Status.SimMissing;
            case 3:
                return Status.SimLocked;
            case 4:
                return Status.SimPukLocked;
            case 5:
                return Status.NetworkLocked;
            case CropImageUtil.MEDIA_DATE_ADDED_INDEX:
                return Status.Normal;
            case CropImageUtil.MEDIA_DATE_MODIFIED_INDEX:
                return Status.SimIOError;
            case 8:
                return Status.NetworkSubsetLocked;
            case CropImageUtil.MEDIA_ORIENTATION_OR_DURATION_INDEX:
                return Status.CorporateLocked;
            case 10:
                return Status.ServiceProviderLocked;
            case 11:
                return Status.SimSimLocked;
            case 12:
                return Status.RuimNetwork1Locked;
            case 13:
                return Status.RuimNetwork2Locked;
            case 14:
                return Status.RuimHrpdLocked;
            case com.nanoha.smartClock.Util.DEFAULT_AMPM_SIZE:
                return Status.RuimCorporateLocked;
            case 16:
                return Status.RuimServiceProviderLocked;
            case 17:
                return Status.RuimRuimLocked;
            case 18:
                return Status.SimMissing;
            default:
                return Status.SimMissing;
        }
    }

    private void updateLayout(Status status) {
        this.mEmergencyCallButton.setVisibility(8);
        this.mIccText = getCurrentText();
        switch ($SWITCH_TABLE$com$nanoha$MyLockScreen_all$LockScreen$Status()[status.ordinal()]) {
            case 1:
                this.mCarrier.setText(getCarrierString(this.mUpdateMonitor.getTelephonyPlmn(), this.mUpdateMonitor.getTelephonySpn()));
                this.mScreenLocked.setText("");
                this.mScreenLocked.setVisibility(0);
                this.mEmergencyCallText.setVisibility(8);
                break;
            case 2:
                this.mCarrier.setText(this.mIccText.networkLockedMessage);
                this.mScreenLocked.setText(this.mIccText.iccInstructionsWhenPatternDisabled);
                this.mScreenLocked.setVisibility(0);
                this.mEmergencyCallText.setVisibility(8);
                break;
            case 3:
                this.mCarrier.setText(this.mIccText.iccMissingMessageShort);
                this.mScreenLocked.setText(this.mIccText.iccInstructionsWhenPatternDisabled);
                this.mScreenLocked.setVisibility(0);
                this.mEmergencyCallText.setVisibility(0);
                break;
            case 4:
                this.mCarrier.setText(this.mIccText.iccMissingMessageShort);
                this.mScreenLocked.setText(this.mIccText.iccMissingInstructions);
                this.mScreenLocked.setVisibility(0);
                this.mEmergencyCallText.setVisibility(0);
                this.mEmergencyCallButton.setVisibility(0);
                break;
            case 5:
                this.mCarrier.setText(this.mIccText.iccPukLockedMessage);
                this.mScreenLocked.setText(this.mIccText.iccPukLockedInstructions);
                this.mScreenLocked.setVisibility(0);
                this.mEmergencyCallText.setVisibility(0);
                this.mEmergencyCallButton.setVisibility(0);
                break;
            case CropImageUtil.MEDIA_DATE_ADDED_INDEX:
                this.mCarrier.setText(this.mIccText.iccPinLockedMessage);
                this.mScreenLocked.setVisibility(4);
                this.mEmergencyCallText.setVisibility(8);
                break;
            case CropImageUtil.MEDIA_DATE_MODIFIED_INDEX:
                this.mCarrier.setText(this.mIccText.iccErrorMessageShort);
                this.mScreenLocked.setText(this.mIccText.iccInstructionsWhenPatternDisabled);
                this.mScreenLocked.setVisibility(4);
                this.mEmergencyCallButton.setVisibility(0);
                break;
            case 8:
                this.mCarrier.setText(R.string.lockscreen_sim_network_subset_locked_message);
                updateLayoutForPersoText();
                break;
            case CropImageUtil.MEDIA_ORIENTATION_OR_DURATION_INDEX:
                this.mCarrier.setText(R.string.lockscreen_sim_corporate_locked_message);
                updateLayoutForPersoText();
                break;
            case 10:
                this.mCarrier.setText(R.string.lockscreen_sim_service_provider_locked_message);
                updateLayoutForPersoText();
                break;
            case 11:
                this.mCarrier.setText(R.string.lockscreen_sim_sim_locked_message);
                updateLayoutForPersoText();
                break;
            case 12:
                this.mCarrier.setText(R.string.lockscreen_ruim_network1_locked_message);
                updateLayoutForPersoText();
                break;
            case 13:
                this.mCarrier.setText(R.string.lockscreen_ruim_network2_locked_message);
                updateLayoutForPersoText();
                break;
            case 14:
                this.mCarrier.setText(R.string.lockscreen_ruim_hrpd_locked_message);
                updateLayoutForPersoText();
                break;
            case com.nanoha.smartClock.Util.DEFAULT_AMPM_SIZE:
                this.mCarrier.setText(R.string.lockscreen_ruim_corporate_locked_message);
                updateLayoutForPersoText();
                break;
            case 16:
                this.mCarrier.setText(R.string.lockscreen_ruim_service_provider_locked_message);
                updateLayoutForPersoText();
                break;
            case 17:
                this.mCarrier.setText(R.string.lockscreen_ruim_ruim_locked_message);
                updateLayoutForPersoText();
                break;
        }
        this.mScreenLocked.setVisibility(4);
        this.mEmergencyCallButton.setVisibility(4);
        this.mEmergencyCallText.setVisibility(4);
    }

    private void updateLayoutForPersoText() {
        this.mScreenLocked.setText(R.string.lockscreen_instructions_when_pattern_disabled);
        this.mScreenLocked.setVisibility(0);
        this.mEmergencyCallButton.setVisibility(8);
    }

    static CharSequence getCarrierString(CharSequence telephonyPlmn, CharSequence telephonySpn) {
        if (telephonyPlmn != null && telephonySpn == null) {
            return telephonyPlmn;
        }
        if (telephonyPlmn != null && telephonySpn != null) {
            return telephonyPlmn + "|" + telephonySpn;
        }
        if (telephonyPlmn != null || telephonySpn == null) {
            return "";
        }
        return telephonySpn;
    }

    public void onSimStateChanged(IccCard.State simState) {
        this.mStatus = getCurrentStatus(simState);
        updateLayout(this.mStatus);
        updateStatusLines();
    }

    public boolean needsInput() {
        return DBG;
    }

    public void onPause() {
    }

    public void onResume() {
        resetStatusInfo(this.mUpdateMonitor);
        updateEmergencyCallButtonState(this.mEmergencyCallButton);
        this.mUpdateMonitor.onResume(this.mContext);
    }

    public void onStop() {
        this.mUpdateMonitor.onStop(this.mContext);
    }

    public void cleanUp() {
        this.mUpdateMonitor.removeCallback(this);
    }

    public void onPhoneStateChanged(String newState) {
        updateEmergencyCallButtonState(this.mEmergencyCallButton);
    }

    private class IccText {
        int iccErrorMessageShort;
        int iccInstructionsWhenPatternDisabled;
        int iccMissingInstructions;
        int iccMissingMessageShort;
        int iccPinLockedMessage;
        int iccPukLockedInstructions;
        int iccPukLockedMessage;
        int networkLockedMessage;

        private IccText() {
        }

        /* synthetic */ IccText(LockScreen lockScreen, IccText iccText) {
            this();
        }
    }

    private IccText createSimText() {
        IccText simText = new IccText(this, (IccText) null);
        simText.iccPukLockedMessage = R.string.lockscreen_sim_puk_locked_message;
        simText.iccPukLockedInstructions = R.string.lockscreen_sim_puk_locked_instructions;
        simText.iccMissingInstructions = R.string.lockscreen_missing_sim_instructions;
        simText.iccInstructionsWhenPatternDisabled = R.string.lockscreen_instructions_when_pattern_disabled;
        simText.iccPinLockedMessage = R.string.lockscreen_pin_locked_message;
        simText.iccMissingMessageShort = R.string.lockscreen_missing_sim_message_short;
        simText.iccErrorMessageShort = R.string.lockscreen_sim_error_message_short;
        simText.networkLockedMessage = R.string.lockscreen_sim_network_locked_message;
        return simText;
    }

    private IccText createRuimText() {
        IccText ruimText = new IccText(this, (IccText) null);
        ruimText.iccPukLockedMessage = R.string.lockscreen_ruim_puk_locked_message;
        ruimText.iccPukLockedInstructions = R.string.lockscreen_ruim_puk_locked_instructions;
        ruimText.iccMissingInstructions = R.string.lockscreen_missing_ruim_instructions;
        ruimText.iccInstructionsWhenPatternDisabled = R.string.lockscreen_instructions_when_pattern_disabled;
        ruimText.iccPinLockedMessage = R.string.lockscreen_pin_locked_message;
        ruimText.iccMissingMessageShort = R.string.lockscreen_missing_ruim_message_short;
        ruimText.iccErrorMessageShort = R.string.lockscreen_ruim_error_message_short;
        ruimText.networkLockedMessage = R.string.lockscreen_ruim_network_locked_message;
        return ruimText;
    }

    private IccText getCurrentText() {
        boolean isGsm = 1 == ((TelephonyManager) this.mContext.getSystemService("phone")).getPhoneType();
        Log.d(TAG, "Updating Lock Screen text to " + (isGsm ? "Sim" : "Ruim"));
        if (isGsm) {
            if (this.mSimText == null) {
                this.mSimText = createSimText();
            }
            return this.mSimText;
        }
        if (this.mRuimText == null) {
            this.mRuimText = createRuimText();
        }
        return this.mRuimText;
    }
}
