package com.nanoha.MyLockScreen_all.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SelectShortcutIcon extends Activity {
    public static final int SELECT_SHORTCUT_ICON = 1003;
    String whichShortcut;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.whichShortcut = getIntent().getStringExtra(ShortcutSetting.SELECT_WHICH_SHORTCUT);
        Intent selectIconintent = new Intent();
        selectIconintent.setType("image/*");
        selectIconintent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(selectIconintent, 1003);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1003 && resultCode == -1) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ShortcutSetting.SHORTCUT_ICON, data.getData().toString());
            resultIntent.putExtra(ShortcutSetting.SELECT_WHICH_SHORTCUT, this.whichShortcut);
            setResult(-1, resultIntent);
        } else {
            setResult(0);
        }
        finish();
    }
}
