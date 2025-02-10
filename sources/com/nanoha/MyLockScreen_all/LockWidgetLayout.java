package com.nanoha.MyLockScreen_all;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.nanoha.util.Util;
import java.util.ArrayList;
import java.util.List;

public class LockWidgetLayout extends ViewGroup {
    private int[] cellInfo = new int[2];
    boolean deleteWidgetMode;
    boolean disableWidgetClick = false;
    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private Context mContext;
    int trashY;
    private Util util;
    private List<Util.WidgetInfo> widgetInfoList;

    public LockWidgetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.util = Util.getInstance(this.mContext);
        this.widgetInfoList = new ArrayList();
        this.widgetInfoList = this.util.loadWidgetInfo();
        initView();
        setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.disableWidgetClick = !this.util.isEnableWidgetClick();
        if (this.disableWidgetClick) {
            disableWidgetClick();
        }
    }

    private void disableWidgetClick() {
        RelativeLayout rl = new RelativeLayout(this.mContext);
        LayoutParams fillParams = new LayoutParams(-1, -1);
        fillParams.fill = true;
        rl.setClickable(false);
        rl.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        addView(rl, fillParams);
    }

    private void initView() {
        this.mAppWidgetManager = AppWidgetManager.getInstance(this.mContext);
        this.mAppWidgetHost = new AppWidgetHost(this.mContext, SelectWidgetActivity.APPWIDGET_HOST_ID);
        this.mAppWidgetHost.startListening();
        for (int i = 0; i < this.widgetInfoList.size(); i++) {
            Util.WidgetInfo info = this.widgetInfoList.get(i);
            AppWidgetProviderInfo appWidgetInfo = this.mAppWidgetManager.getAppWidgetInfo(info.appWidgetId);
            if (!(appWidgetInfo == null || info == null)) {
                View hostView = this.mAppWidgetHost.createView(this.mContext, info.appWidgetId, appWidgetInfo);
                this.cellInfo[0] = info.x;
                this.cellInfo[1] = info.y;
                int widgetWidth = info.lp.width;
                int widgetHeight = info.lp.height;
                if (widgetWidth <= 0 || widgetHeight <= 0) {
                    widgetWidth = appWidgetInfo.minWidth % 74 == 0 ? appWidgetInfo.minWidth : ((appWidgetInfo.minWidth / 74) + 1) * 74;
                    widgetHeight = appWidgetInfo.minHeight % 74 == 0 ? appWidgetInfo.minHeight : ((appWidgetInfo.minHeight / 74) + 1) * 74;
                }
                addInScreen(info.appWidgetId, hostView, widgetWidth, widgetHeight);
            }
        }
    }

    public void saveWidgetInfo() {
        this.util.saveWidgetInfo(this.widgetInfoList);
    }

    public void addInScreen(int appWidgetId, View child, int width, int height, boolean saveInList) {
        addInScreen(appWidgetId, child, width, height);
        if (saveInList) {
            Util util2 = this.util;
            util2.getClass();
            Util.WidgetInfo info = new Util.WidgetInfo();
            info.appWidgetId = appWidgetId;
            info.x = this.cellInfo[0];
            info.y = this.cellInfo[1];
            this.widgetInfoList.add(info);
        }
    }

    public void addInScreen(int appWidgetId, View child, int width, int height) {
        LayoutParams lp = new LayoutParams(width, height);
        lp.x = this.cellInfo[0];
        lp.y = this.cellInfo[1];
        addView(child, lp);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int index = 0; index < getChildCount(); index++) {
            LayoutParams lp = (LayoutParams) getChildAt(index).getLayoutParams();
            if (lp.fill) {
                getChildAt(index).measure(widthMeasureSpec, heightMeasureSpec);
            } else {
                getChildAt(index).measure(View.MeasureSpec.makeMeasureSpec(1073741824, lp.width), View.MeasureSpec.makeMeasureSpec(1073741824, lp.height));
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        this.cellInfo[0] = (int) event.getX();
        this.cellInfo[1] = (int) event.getY();
        return super.dispatchTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        for (int index = 0; index < getChildCount(); index++) {
            LayoutParams lp = (LayoutParams) getChildAt(index).getLayoutParams();
            if (lp.fill) {
                getChildAt(index).layout(0, 0, width, height);
            } else {
                getChildAt(index).layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
            }
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        boolean fill = false;
        int x;
        int y;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
