package com.nanoha.MyLockScreen_all;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.nanoha.util.Util;
import java.util.ArrayList;

public class SelectWidgetActivity extends Activity {
    public static final int APPWIDGET_HOST_ID = 256;
    private static final String EXTRA_CUSTOM_WIDGET = "custom_widget";
    private static final int MSG_ADD_WIDGET = 1001;
    private static final int REQUEST_CREATE_APPWIDGET = 2;
    private static final int REQUEST_PICK_APPWIDGET = 1;
    private WidgetLayout layout;
    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    SelectWidgetActivity.this.addWidget();
                    return;
                default:
                    return;
            }
        }
    };
    Util util;

    public void onCreate(Bundle savedInstanceState) {
        this.util = Util.getInstance(this);
        String background = this.util.getBackground();
        if ("0".equals(background) && this.util.isDisplayStatusbar()) {
            setTheme(16973919);
        } else if ("0".equals(background) && !this.util.isDisplayStatusbar()) {
            setTheme(16973920);
        } else if ("0".equals(background) || this.util.isDisplayStatusbar()) {
            setTheme(16973830);
        } else {
            setTheme(16973831);
        }
        super.onCreate(savedInstanceState);
        this.mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        this.mAppWidgetHost = new AppWidgetHost(getApplicationContext(), APPWIDGET_HOST_ID);
        this.mAppWidgetHost.startListening();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.layout = new WidgetLayout(this, dm.widthPixels, dm.heightPixels);
        this.layout.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                SelectWidgetActivity.this.mHandler.removeMessages(1001);
                SelectWidgetActivity.this.mHandler.sendEmptyMessage(1001);
                return false;
            }
        });
        String screenOrientation = this.util.getScreenOrientation();
        if (screenOrientation.equals(Util.SCREEN_ORIENTATION_HORIZONTAL)) {
            setRequestedOrientation(0);
        } else if (screenOrientation.equals(Util.SCREEN_ORIENTATION_VERTICAL)) {
            setRequestedOrientation(1);
        } else if (dm.widthPixels > dm.heightPixels) {
            setRequestedOrientation(0);
        } else {
            setRequestedOrientation(1);
        }
        LinearLayout screenLayout = new LockScreenManager(this).createSelectWidgetLockScreen(this.layout);
        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.addView(screenLayout);
        mainLayout.addView(this.layout, new LinearLayout.LayoutParams(-1, -1));
        setContentView(mainLayout);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.layout.saveWidgetInfo();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int appWidgetId;
        if (resultCode == -1) {
            switch (requestCode) {
                case 1:
                    addAppWidget(data);
                    return;
                case 2:
                    completeAddAppWidget(data);
                    return;
                default:
                    return;
            }
        } else if (requestCode == 1 && resultCode == 0 && data != null && (appWidgetId = data.getIntExtra("appWidgetId", -1)) != -1) {
            this.mAppWidgetHost.deleteAppWidgetId(appWidgetId);
        }
    }

    private void addAppWidget(Intent data) {
        int appWidgetId = data.getIntExtra("appWidgetId", -1);
        String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
        Log.d("addAppWidget", "data:" + customWidget);
        if ("search_widget".equals(customWidget)) {
            this.mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            return;
        }
        AppWidgetProviderInfo appWidget = this.mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        Log.d("addAppWidget", "configure:" + appWidget.configure);
        if (appWidget.configure != null) {
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
            intent.setComponent(appWidget.configure);
            intent.putExtra("appWidgetId", appWidgetId);
            startActivityForResult(intent, 2);
            return;
        }
        onActivityResult(2, -1, data);
    }

    /* access modifiers changed from: private */
    public void addWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent("android.appwidget.action.APPWIDGET_PICK");
        pickIntent.putExtra("appWidgetId", appWidgetId);
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<>();
        AppWidgetProviderInfo info = new AppWidgetProviderInfo();
        info.provider = new ComponentName(getPackageName(), "XXX.YYY");
        info.label = "Search";
        info.icon = R.drawable.ic_search_widget;
        customInfo.add(info);
        pickIntent.putParcelableArrayListExtra("customInfo", customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<>();
        Bundle b = new Bundle();
        b.putString(EXTRA_CUSTOM_WIDGET, "search_widget");
        customExtras.add(b);
        pickIntent.putParcelableArrayListExtra("customExtras", customExtras);
        startActivityForResult(pickIntent, 1);
    }

    private void completeAddAppWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt("appWidgetId", -1);
        Log.d("completeAddAppWidget", "dumping extras content=" + extras.toString());
        Log.d("completeAddAppWidget", "appWidgetId:" + appWidgetId);
        AppWidgetProviderInfo appWidgetInfo = this.mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        this.layout.addInScreen(appWidgetId, this.mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo), appWidgetInfo.minWidth % 74 == 0 ? appWidgetInfo.minWidth : ((appWidgetInfo.minWidth / 74) + 1) * 74, appWidgetInfo.minHeight % 74 == 0 ? appWidgetInfo.minHeight : ((appWidgetInfo.minHeight / 74) + 1) * 74, true);
    }
}
