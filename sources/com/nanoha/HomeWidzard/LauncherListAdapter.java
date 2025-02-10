package com.nanoha.HomeWidzard;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nanoha.MyLockScreen_all.R;
import java.util.List;

public class LauncherListAdapter extends BaseAdapter {
    private List<LauncherData> launcherDataList;
    private Context mContext;
    private LayoutInflater mInflater = LayoutInflater.from(this.mContext);
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public LauncherListAdapter(Context ctx, List<LauncherData> launcherDataList2) {
        this.mContext = ctx;
        this.options.inJustDecodeBounds = false;
        this.options.inSampleSize = 2;
        this.launcherDataList = launcherDataList2;
    }

    public int getCount() {
        return this.launcherDataList.size();
    }

    public Object getItem(int position) {
        return this.launcherDataList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = this.mInflater.inflate(R.layout.select_launcher, (ViewGroup) null);
        LauncherDataHolder viewHolder = new LauncherDataHolder((LauncherDataHolder) null);
        viewHolder.launcherImg = (ImageView) convertView2.findViewById(R.id.launcherImg);
        viewHolder.launcherText = (TextView) convertView2.findViewById(R.id.launcherText);
        LauncherData launcherData = (LauncherData) getItem(position);
        if (launcherData != null) {
            viewHolder.launcherImg.setBackgroundDrawable(launcherData.icon);
            viewHolder.launcherText.setText(launcherData.name);
        }
        return convertView2;
    }

    private static final class LauncherDataHolder {
        ImageView launcherImg;
        TextView launcherText;

        private LauncherDataHolder() {
        }

        /* synthetic */ LauncherDataHolder(LauncherDataHolder launcherDataHolder) {
            this();
        }
    }
}
