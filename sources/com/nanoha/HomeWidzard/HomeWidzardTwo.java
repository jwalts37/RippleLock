package com.nanoha.HomeWidzard;

import android.app.Activity;
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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;
import java.util.ArrayList;
import java.util.List;

public class HomeWidzardTwo extends Activity {
    ListView launcher_list;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Util util = Util.getInstance(this);
        util.saveValue(Util.IN_HOME_WIDZARD, false);
        setContentView(R.layout.home_widzard_two);
        final List<LauncherData> launcherList = getLauncherList();
        if (launcherList.size() > 0) {
            this.launcher_list = (ListView) findViewById(R.id.launcher_list);
            this.launcher_list.setAdapter(new LauncherListAdapter(this, getLauncherList()));
            this.launcher_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                    LauncherData launcherData = (LauncherData) launcherList.get(position);
                    util.saveValue(Util.USE_LAUNCHER_PACKAGE_NAME, launcherData.packageName);
                    util.saveValue(Util.USE_LAUNCHER_CLASS_NAME, launcherData.className);
                    Toast.makeText(HomeWidzardTwo.this, R.string.finish_home_widzard, 1).show();
                    HomeWidzardTwo.this.finish();
                }
            });
        }
    }

    private List<LauncherData> getLauncherList() {
        List<LauncherData> list = new ArrayList<>();
        PackageManager localPackageManager = getPackageManager();
        Intent launcherIntent = new Intent("android.intent.action.MAIN");
        launcherIntent.addCategory("android.intent.category.HOME");
        launcherIntent.addCategory("android.intent.category.DEFAULT");
        List<ResolveInfo> launcherList = localPackageManager.queryIntentActivities(launcherIntent, 0);
        Resources resources = getResources();
        int size = (int) resources.getDimension(17104896);
        IconResizer sResizer = new IconResizer(size, size, resources.getDisplayMetrics());
        PackageManager pm = getPackageManager();
        String thisPackageName = getPackageName();
        for (int i = 0; i < launcherList.size(); i++) {
            ResolveInfo launcherInfo = launcherList.get(i);
            String packageName = launcherInfo.activityInfo.applicationInfo.packageName;
            if (!thisPackageName.equals(packageName)) {
                LauncherData launcherData = new LauncherData();
                launcherData.name = launcherInfo.loadLabel(pm);
                launcherData.icon = sResizer.createIconThumbnail(launcherInfo.loadIcon(pm));
                launcherData.packageName = packageName;
                launcherData.className = launcherInfo.activityInfo.name;
                list.add(launcherData);
            }
        }
        return list;
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
