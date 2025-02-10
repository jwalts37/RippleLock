package com.nanoha.MyLockScreen_all.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.nanoha.MyLockScreen_all.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectAppActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final int UPDATE_RESULT = 1;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int size = SelectAppActivity.this.mSearchData.size();
                    if (size >= 7) {
                        SelectAppActivity.this.viewNoResult.setVisibility(8);
                    } else if (size != 0) {
                        SelectAppActivity.this.mSearchData.add((Object) null);
                        SelectAppActivity.this.viewNoResult.setVisibility(8);
                    } else {
                        SelectAppActivity.this.viewNoResult.setTextColor(-16777216);
                        SelectAppActivity.this.viewNoResult.setVisibility(0);
                    }
                    SelectAppActivity.this.mAdapter = new AppListAdapter(SelectAppActivity.this, SelectAppActivity.this.mSearchData);
                    SelectAppActivity.this.mListView.setAdapter(SelectAppActivity.this.mAdapter);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public AppListAdapter mAdapter;
    private EditText mAppFilter;
    /* access modifiers changed from: private */
    public List<ResolveInfo> mData;
    /* access modifiers changed from: private */
    public ListView mListView;
    /* access modifiers changed from: private */
    public PackageManager mPackageManager;
    /* access modifiers changed from: private */
    public List<ResolveInfo> mSearchData = new ArrayList();
    /* access modifiers changed from: private */
    public TextView viewNoResult;
    private TextWatcher watcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SelectAppActivity.this.mSearchData.clear();
            if (SelectAppActivity.this.mData != null && SelectAppActivity.this.mData.size() > 0) {
                for (int i = 0; i < SelectAppActivity.this.mData.size(); i++) {
                    if (((ResolveInfo) SelectAppActivity.this.mData.get(i)).loadLabel(SelectAppActivity.this.mPackageManager).toString().toLowerCase().contains(s.toString().toLowerCase())) {
                        SelectAppActivity.this.mSearchData.add((ResolveInfo) SelectAppActivity.this.mData.get(i));
                    }
                }
                Message message = new Message();
                message.what = 1;
                SelectAppActivity.this.handler.sendMessage(message);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    String whichShortcut;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.whichShortcut = getIntent().getStringExtra(ShortcutSetting.SELECT_WHICH_SHORTCUT);
        this.mPackageManager = getPackageManager();
        View mainView = LayoutInflater.from(this).inflate(R.layout.select_app_mail, (ViewGroup) null);
        this.viewNoResult = (TextView) mainView.findViewById(R.id.empty_text);
        this.mListView = (ListView) mainView.findViewById(R.id.all_app_list);
        this.mListView.setOnItemClickListener(this);
        this.mData = getListData();
        this.mAdapter = new AppListAdapter(this, this.mData);
        this.mListView.setAdapter(this.mAdapter);
        this.mAppFilter = (EditText) mainView.findViewById(R.id.app_filter);
        this.mAppFilter.addTextChangedListener(this.watcher);
        setContentView(mainView);
    }

    private List<ResolveInfo> getListData() {
        List<ResolveInfo> list = new ArrayList<>();
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> allAppList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(allAppList, new ResolveInfo.DisplayNameComparator(packageManager));
        for (ResolveInfo resolveInfo : allAppList) {
            if (resolveInfo.activityInfo != null) {
                list.add(resolveInfo);
            }
        }
        return list;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ResolveInfo selectItem = (ResolveInfo) this.mAdapter.getItem(position);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ShortcutSetting.SELECT_WHICH_SHORTCUT, this.whichShortcut);
        resultIntent.setClassName(selectItem.activityInfo.applicationInfo.packageName, selectItem.activityInfo.name);
        setResult(-1, resultIntent);
        finish();
    }

    public void onBackPressed() {
        setResult(0, (Intent) null);
        finish();
        super.onBackPressed();
    }

    private class AppListAdapter extends BaseAdapter {
        private Context mContext;
        private List<ResolveInfo> mList;
        private PackageManager mPackageManager;

        public AppListAdapter(Context context, List<ResolveInfo> list) {
            this.mContext = context;
            this.mPackageManager = SelectAppActivity.this.getPackageManager();
            this.mList = list;
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int position) {
            return this.mList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.select_app_item, (ViewGroup) null);
            }
            TextView textView = (TextView) convertView;
            if (this.mList.get(position) != null) {
                textView.setText(this.mList.get(position).loadLabel(this.mPackageManager));
                Drawable icon = this.mList.get(position).loadIcon(this.mPackageManager);
                Resources resources = SelectAppActivity.this.getResources();
                int size = (int) resources.getDimension(17104896);
                textView.setCompoundDrawablesWithIntrinsicBounds(new IconResizer(size, size, resources.getDisplayMetrics()).createIconThumbnail(icon), (Drawable) null, (Drawable) null, (Drawable) null);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
            return convertView;
        }
    }

    private static class IconResizer {
        private final Canvas mCanvas = new Canvas();
        private final int mIconHeight;
        private final int mIconWidth;
        private final DisplayMetrics mMetrics;
        private final Rect mOldBounds = new Rect();

        public IconResizer(int width, int height, DisplayMetrics metrics) {
            this.mCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
            this.mMetrics = metrics;
            this.mIconWidth = width;
            this.mIconHeight = height;
        }

        public Drawable createIconThumbnail(Drawable icon) {
            int width = this.mIconWidth;
            int height = this.mIconHeight;
            if (icon instanceof PaintDrawable) {
                PaintDrawable painter = (PaintDrawable) icon;
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            } else if (icon instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
                if (bitmapDrawable.getBitmap().getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(this.mMetrics);
                }
            }
            int iconWidth = icon.getIntrinsicWidth();
            int iconHeight = icon.getIntrinsicHeight();
            if (iconWidth <= 0 || iconHeight <= 0) {
                return icon;
            }
            if (width < iconWidth || height < iconHeight) {
                float ratio = ((float) iconWidth) / ((float) iconHeight);
                if (iconWidth > iconHeight) {
                    height = (int) (((float) width) / ratio);
                } else if (iconHeight > iconWidth) {
                    width = (int) (((float) height) * ratio);
                }
                Bitmap thumb = Bitmap.createBitmap(this.mIconWidth, this.mIconHeight, icon.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = this.mCanvas;
                canvas.setBitmap(thumb);
                this.mOldBounds.set(icon.getBounds());
                int x = (this.mIconWidth - width) / 2;
                int y = (this.mIconHeight - height) / 2;
                icon.setBounds(x, y, x + width, y + height);
                icon.draw(canvas);
                icon.setBounds(this.mOldBounds);
                BitmapDrawable bitmapDrawable2 = new BitmapDrawable(thumb);
                bitmapDrawable2.setTargetDensity(this.mMetrics);
                return bitmapDrawable2;
            } else if (iconWidth >= width || iconHeight >= height) {
                return icon;
            } else {
                Bitmap thumb2 = Bitmap.createBitmap(this.mIconWidth, this.mIconHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = this.mCanvas;
                canvas2.setBitmap(thumb2);
                this.mOldBounds.set(icon.getBounds());
                int x2 = (width - iconWidth) / 2;
                int y2 = (height - iconHeight) / 2;
                icon.setBounds(x2, y2, x2 + iconWidth, y2 + iconHeight);
                icon.draw(canvas2);
                icon.setBounds(this.mOldBounds);
                BitmapDrawable bitmapDrawable3 = new BitmapDrawable(thumb2);
                bitmapDrawable3.setTargetDensity(this.mMetrics);
                return bitmapDrawable3;
            }
        }
    }
}
