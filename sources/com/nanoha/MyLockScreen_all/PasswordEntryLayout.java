package com.nanoha.MyLockScreen_all;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nanoha.util.Util;

public class PasswordEntryLayout extends LinearLayout implements View.OnClickListener {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private View mBackSpaceButton;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mEnteredDigits = 0;
    private final int[] mEnteredPin = new int[8];
    /* access modifiers changed from: private */
    public TextView mHeaderText;
    private TextView mOkButton;
    /* access modifiers changed from: private */
    public TextView mPinText;
    Intent quickIntent;
    Util util;

    public PasswordEntryLayout(Context context, Intent quickIntent2, boolean isPortrait) {
        super(context);
        this.mContext = context;
        this.quickIntent = quickIntent2;
        this.util = Util.getInstance(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (isPortrait) {
            inflater.inflate(R.layout.password_entry, this, true);
        } else {
            inflater.inflate(R.layout.password_entry_landscape, this, true);
        }
        new TouchInput(this, (TouchInput) null);
        this.mHeaderText = (TextView) findViewById(R.id.headerText);
        this.mPinText = (TextView) findViewById(R.id.pinDisplay);
        this.mBackSpaceButton = findViewById(R.id.backspace);
        this.mBackSpaceButton.setOnClickListener(this);
        this.mOkButton = (TextView) findViewById(R.id.ok);
        this.mPinText.setFocusable(false);
        this.mOkButton.setOnClickListener(this);
        setFocusableInTouchMode(true);
    }

    public void setQuickIntent(Intent quickIntent2) {
        this.quickIntent = quickIntent2;
    }

    public boolean needsInput() {
        return true;
    }

    public void onPause() {
    }

    public void onResume() {
        this.mPinText.setText("");
        this.mEnteredDigits = 0;
    }

    private abstract class CheckSimPin extends Thread {
        private final String mPin;

        /* access modifiers changed from: package-private */
        public abstract void onSimLockChangedResponse(boolean z);

        protected CheckSimPin(String pin) {
            this.mPin = pin;
        }

        public void run() {
            try {
                final boolean result = PasswordEntryLayout.this.util.isRightPassword(this.mPin);
                PasswordEntryLayout.this.post(new Runnable() {
                    public void run() {
                        CheckSimPin.this.onSimLockChangedResponse(result);
                    }
                });
            } catch (Exception e) {
                Exception exc = e;
                PasswordEntryLayout.this.post(new Runnable() {
                    public void run() {
                        CheckSimPin.this.onSimLockChangedResponse(false);
                    }
                });
            }
        }
    }

    public void onClick(View v) {
        if (v == this.mBackSpaceButton) {
            Editable digits = this.mPinText.getEditableText();
            int len = digits.length();
            if (len > 0) {
                digits.delete(len - 1, len);
                this.mEnteredDigits--;
            }
        } else if (v == this.mOkButton) {
            checkPin();
        }
    }

    private void checkPin() {
        new CheckSimPin(this.mPinText.getText().toString()) {
            /* Debug info: failed to restart local var, previous not found, register: 2 */
            /* access modifiers changed from: package-private */
            public void onSimLockChangedResponse(boolean success) {
                if (success) {
                    if (PasswordEntryLayout.this.quickIntent != null) {
                        PasswordEntryLayout.this.mContext.startActivity(PasswordEntryLayout.this.quickIntent);
                    }
                    ((PasswordActivity) PasswordEntryLayout.this.mContext).finish();
                    return;
                }
                PasswordEntryLayout.this.mHeaderText.setText(R.string.password_wrong);
                PasswordEntryLayout.this.mPinText.setText("");
                PasswordEntryLayout.this.mEnteredDigits = 0;
            }
        }.start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.mContext.startActivity(new Intent(this.mContext, CircleLockScreen.class));
            return true;
        }
        char match = event.getMatch(DIGITS);
        if (match != 0) {
            reportDigit(match - '0');
            return true;
        } else if (keyCode == 67) {
            if (this.mEnteredDigits > 0) {
                this.mPinText.onKeyDown(keyCode, event);
                this.mEnteredDigits--;
            }
            return true;
        } else if (keyCode != 66) {
            return false;
        } else {
            checkPin();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void reportDigit(int digit) {
        if (this.mEnteredDigits == 0) {
            this.mPinText.setText("");
        }
        if (this.mEnteredDigits != 8) {
            this.mPinText.append(Integer.toString(digit));
            int[] iArr = this.mEnteredPin;
            int i = this.mEnteredDigits;
            this.mEnteredDigits = i + 1;
            iArr[i] = digit;
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class TouchInput implements View.OnClickListener {
        private TextView mCancelButton;
        private TextView mEight;
        private TextView mFive;
        private TextView mFour;
        private TextView mNine;
        private TextView mOne;
        private TextView mSeven;
        private TextView mSix;
        private TextView mThree;
        private TextView mTwo;
        private TextView mZero;

        /* synthetic */ TouchInput(PasswordEntryLayout passwordEntryLayout, TouchInput touchInput) {
            this();
        }

        private TouchInput() {
            this.mZero = (TextView) PasswordEntryLayout.this.findViewById(R.id.zero);
            this.mOne = (TextView) PasswordEntryLayout.this.findViewById(R.id.one);
            this.mTwo = (TextView) PasswordEntryLayout.this.findViewById(R.id.two);
            this.mThree = (TextView) PasswordEntryLayout.this.findViewById(R.id.three);
            this.mFour = (TextView) PasswordEntryLayout.this.findViewById(R.id.four);
            this.mFive = (TextView) PasswordEntryLayout.this.findViewById(R.id.five);
            this.mSix = (TextView) PasswordEntryLayout.this.findViewById(R.id.six);
            this.mSeven = (TextView) PasswordEntryLayout.this.findViewById(R.id.seven);
            this.mEight = (TextView) PasswordEntryLayout.this.findViewById(R.id.eight);
            this.mNine = (TextView) PasswordEntryLayout.this.findViewById(R.id.nine);
            this.mCancelButton = (TextView) PasswordEntryLayout.this.findViewById(R.id.cancel);
            this.mZero.setText("0");
            this.mOne.setText("1");
            this.mTwo.setText("2");
            this.mThree.setText("3");
            this.mFour.setText("4");
            this.mFive.setText("5");
            this.mSix.setText("6");
            this.mSeven.setText("7");
            this.mEight.setText("8");
            this.mNine.setText("9");
            this.mZero.setOnClickListener(this);
            this.mOne.setOnClickListener(this);
            this.mTwo.setOnClickListener(this);
            this.mThree.setOnClickListener(this);
            this.mFour.setOnClickListener(this);
            this.mFive.setOnClickListener(this);
            this.mSix.setOnClickListener(this);
            this.mSeven.setOnClickListener(this);
            this.mEight.setOnClickListener(this);
            this.mNine.setOnClickListener(this);
            this.mCancelButton.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (v == this.mCancelButton) {
                PasswordEntryLayout.this.mContext.startActivity(new Intent(PasswordEntryLayout.this.mContext, CircleLockScreen.class));
                return;
            }
            int digit = checkDigit(v);
            if (digit >= 0) {
                PasswordEntryLayout.this.reportDigit(digit);
            }
        }

        private int checkDigit(View v) {
            if (v == this.mZero) {
                return 0;
            }
            if (v == this.mOne) {
                return 1;
            }
            if (v == this.mTwo) {
                return 2;
            }
            if (v == this.mThree) {
                return 3;
            }
            if (v == this.mFour) {
                return 4;
            }
            if (v == this.mFive) {
                return 5;
            }
            if (v == this.mSix) {
                return 6;
            }
            if (v == this.mSeven) {
                return 7;
            }
            if (v == this.mEight) {
                return 8;
            }
            if (v == this.mNine) {
                return 9;
            }
            return -1;
        }
    }
}
