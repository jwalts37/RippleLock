package com.nanoha.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.nanoha.MyLockScreen_all.CircleLockScreen;

public class AnswerCallScreen extends Activity {
    public static final int REQUEST_CODE_FROM_CALL = 1001;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setTheme(16973835);
        super.onCreate(savedInstanceState);
        Intent senseIntent = new Intent(this, CircleLockScreen.class);
        startActivityForResult(senseIntent, 1001);
        startActivity(senseIntent);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}
