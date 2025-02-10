package com.nanoha.MyLockScreen_all;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
    public static final int SORT_ANSWER_CALL = 3;
    public static final int SORT_END_CALL = 2;
    public static final int SORT_QUICK_VIEW = 1;
    public Intent intent;
    public boolean isBroadCast = false;
    public int sort = 1;

    public CustomImageView(Context context) {
        super(context);
    }

    public void setIntent(Intent intent2) {
        this.intent = intent2;
    }
}
