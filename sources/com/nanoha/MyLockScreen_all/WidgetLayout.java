package com.nanoha.MyLockScreen_all;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.nanoha.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetLayout extends ViewGroup {
    private int[] cellInfo = new int[2];
    boolean deleteWidgetMode;
    LinearLayout imageButtonLayout;
    boolean isLandscape = false;
    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private Context mContext;
    private View.OnLongClickListener mLongClickListener;
    private View.OnTouchListener mOnTouchListener;
    ImageButton minusButton;
    ImageButton plusButton;
    ImageView trashView;
    int trashX;
    int trashY;
    private Util util;
    /* access modifiers changed from: private */
    public List<Util.WidgetInfo> widgetInfoList;
    int widgetLayout_height;
    int widgetLayout_width;
    /* access modifiers changed from: private */
    public Map<View, View> widgetMap;

    public WidgetLayout(Context context, int widgetLayout_width2, int widgetLayout_height2) {
        super(context);
        this.mContext = context;
        this.util = Util.getInstance(context);
        this.widgetLayout_width = widgetLayout_width2;
        this.widgetLayout_height = widgetLayout_height2;
        if (widgetLayout_width2 > widgetLayout_height2) {
            this.isLandscape = true;
        } else {
            this.isLandscape = false;
        }
        this.widgetInfoList = new ArrayList();
        this.widgetMap = new HashMap();
        initListener();
        this.widgetInfoList = this.util.loadWidgetInfo();
        initView();
    }

    private void initView() {
        int trashViewLength;
        int widgetHeight;
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
                int widgetHeight2 = info.lp.height;
                if (widgetWidth <= 0 || widgetHeight2 <= 0) {
                    int widgetWidth2 = appWidgetInfo.minWidth % 74 == 0 ? appWidgetInfo.minWidth : ((appWidgetInfo.minWidth / 74) + 1) * 74;
                    if (appWidgetInfo.minHeight % 74 == 0) {
                        widgetHeight = appWidgetInfo.minHeight;
                    } else {
                        widgetHeight = ((appWidgetInfo.minHeight / 74) + 1) * 74;
                    }
                    info.lp.width = widgetWidth2;
                    info.lp.height = widgetHeight;
                }
                addInScreen(info.appWidgetId, hostView, info.lp);
            }
        }
        this.trashView = new ImageView(this.mContext);
        this.trashView.setBackgroundResource(R.drawable.trashcan);
        if (this.isLandscape) {
            trashViewLength = this.widgetLayout_height / 6;
            this.trashX = (this.widgetLayout_width - trashViewLength) - 25;
            this.trashY = ((this.widgetLayout_height - trashViewLength) - 25) / 2;
        } else {
            trashViewLength = this.widgetLayout_width / 6;
            this.trashX = (this.widgetLayout_width - trashViewLength) / 2;
            this.trashY = (this.widgetLayout_height - trashViewLength) - 25;
        }
        LayoutParams trashLp = new LayoutParams(trashViewLength, trashViewLength);
        trashLp.x = this.trashX;
        trashLp.y = this.trashY;
        addView(this.trashView, trashLp);
    }

    private void initListener() {
        this.mLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                v.setClickable(false);
                v.setBackgroundColor(Color.argb(100, 0, 255, 0));
                return true;
            }
        };
        this.mOnTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (v.isClickable()) {
                    return false;
                }
                View parent = (View) WidgetLayout.this.widgetMap.get(v);
                int action = event.getAction();
                int rawX = ((int) event.getRawX()) - (parent.getWidth() / 2);
                int rawY = (((int) event.getRawY()) - (parent.getHeight() / 2)) - 15;
                switch (action) {
                    case 1:
                        WidgetLayout.this.trashView.setBackgroundResource(R.drawable.trashcan);
                        v.setClickable(true);
                        if (!WidgetLayout.this.deleteWidgetMode) {
                            LayoutParams widgetParams = (LayoutParams) parent.getLayoutParams();
                            widgetParams.x = rawX;
                            widgetParams.y = rawY;
                            int i = 0;
                            while (true) {
                                if (i < WidgetLayout.this.widgetInfoList.size()) {
                                    Util.WidgetInfo info = (Util.WidgetInfo) WidgetLayout.this.widgetInfoList.get(i);
                                    if (info.appWidgetId == parent.getId()) {
                                        info.x = rawX;
                                        info.y = rawY;
                                    } else {
                                        i++;
                                    }
                                }
                            }
                            v.setBackgroundColor(Color.argb(0, 255, 0, 0));
                            break;
                        } else {
                            int i2 = 0;
                            while (true) {
                                if (i2 < WidgetLayout.this.widgetInfoList.size()) {
                                    if (((Util.WidgetInfo) WidgetLayout.this.widgetInfoList.get(i2)).appWidgetId == parent.getId()) {
                                        WidgetLayout.this.widgetInfoList.remove(i2);
                                    } else {
                                        i2++;
                                    }
                                }
                            }
                            WidgetLayout.this.widgetMap.remove(v);
                            WidgetLayout.this.removeView(parent);
                            break;
                        }
                    case 2:
                        parent.layout(rawX, rawY, parent.getWidth() + rawX, parent.getHeight() + rawY);
                        if (!WidgetLayout.this.isLandscape) {
                            if (parent.getHeight() + rawY <= WidgetLayout.this.trashY) {
                                WidgetLayout.this.trashView.setBackgroundResource(R.drawable.trashcan);
                                WidgetLayout.this.deleteWidgetMode = false;
                                break;
                            } else {
                                WidgetLayout.this.trashView.setBackgroundResource(R.drawable.trashcan_hover);
                                WidgetLayout.this.deleteWidgetMode = true;
                                break;
                            }
                        } else if (parent.getWidth() + rawX <= WidgetLayout.this.trashX) {
                            WidgetLayout.this.trashView.setBackgroundResource(R.drawable.trashcan);
                            WidgetLayout.this.deleteWidgetMode = false;
                            break;
                        } else {
                            WidgetLayout.this.trashView.setBackgroundResource(R.drawable.trashcan_hover);
                            WidgetLayout.this.deleteWidgetMode = true;
                            break;
                        }
                }
                return false;
            }
        };
    }

    public void saveWidgetInfo() {
        this.util.saveWidgetInfo(this.widgetInfoList);
    }

    public void addInScreen(int appWidgetId, View child, int width, int height, boolean saveInList) {
        LayoutParams lp = new LayoutParams(width, height);
        addInScreen(appWidgetId, child, lp);
        if (saveInList) {
            Util util2 = this.util;
            util2.getClass();
            Util.WidgetInfo info = new Util.WidgetInfo();
            info.appWidgetId = appWidgetId;
            info.x = this.cellInfo[0];
            info.y = this.cellInfo[1];
            info.lp = lp;
            this.widgetInfoList.add(info);
        }
    }

    public void addInScreen(int appWidgetId, final View child, final LayoutParams lp) {
        lp.x = this.cellInfo[0];
        lp.y = this.cellInfo[1];
        final RelativeLayout widgetLayout = new RelativeLayout(this.mContext);
        widgetLayout.setId(appWidgetId);
        widgetLayout.addView(child, lp);
        RelativeLayout tempLayout = new RelativeLayout(this.mContext);
        tempLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
        widgetLayout.addView(tempLayout, new LayoutParams(-1, -1));
        tempLayout.setOnLongClickListener(this.mLongClickListener);
        tempLayout.setOnTouchListener(this.mOnTouchListener);
        View buttonLayout = LayoutInflater.from(this.mContext).inflate(R.layout.widget_image_button, (ViewGroup) null);
        this.plusButton = (ImageButton) buttonLayout.findViewById(R.id.plus_but);
        this.minusButton = (ImageButton) buttonLayout.findViewById(R.id.minus_but);
        this.plusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lp.width += 5;
                lp.height += 5;
                widgetLayout.removeView(child);
                widgetLayout.addView(child, 0, lp);
            }
        });
        this.minusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lp.width -= 5;
                lp.height -= 5;
                widgetLayout.removeView(child);
                widgetLayout.addView(child, 0, lp);
            }
        });
        RelativeLayout.LayoutParams imageButtonLp = new RelativeLayout.LayoutParams(-2, -2);
        imageButtonLp.addRule(12);
        tempLayout.addView(buttonLayout, imageButtonLp);
        addView(widgetLayout, lp);
        this.widgetMap.put(tempLayout, widgetLayout);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int index = 0; index < getChildCount(); index++) {
            LayoutParams lp = (LayoutParams) getChildAt(index).getLayoutParams();
            getChildAt(index).measure(View.MeasureSpec.makeMeasureSpec(1073741824, lp.width), View.MeasureSpec.makeMeasureSpec(1073741824, lp.height));
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        this.cellInfo[0] = 50;
        this.cellInfo[1] = (int) event.getY();
        return super.dispatchTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int index = 0; index < getChildCount(); index++) {
            LayoutParams lp = (LayoutParams) getChildAt(index).getLayoutParams();
            getChildAt(index).layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
