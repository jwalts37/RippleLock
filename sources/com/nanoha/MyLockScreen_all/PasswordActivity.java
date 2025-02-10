package com.nanoha.MyLockScreen_all;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.nanoha.util.Util;
import java.net.URISyntaxException;

public class PasswordActivity extends Activity {
    PasswordEntryLayout passwordLayout;

    public void onCreate(Bundle savedInstanceState) {
        setTheme(16973920);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        this.passwordLayout = new PasswordEntryLayout(this, (Intent) null, true);
        setContentView(this.passwordLayout);
    }

    public void onAttachedToWindow() {
        getWindow().setType(2004);
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Intent quickIntent = null;
        String quickIntentString = getIntent().getStringExtra(Util.QUICK_INTENT_STRING);
        if (quickIntentString != null) {
            try {
                quickIntent = Intent.getIntent(quickIntentString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                quickIntent = null;
            }
        }
        this.passwordLayout.setQuickIntent(quickIntent);
    }
}
