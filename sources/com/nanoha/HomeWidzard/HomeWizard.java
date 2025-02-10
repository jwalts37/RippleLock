package com.nanoha.HomeWidzard;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.R;
import com.nanoha.util.Util;

public class HomeWizard extends Activity {
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String SCHEME = "package";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.getInstance(this).saveValue(Util.IN_HOME_WIDZARD, true);
        setContentView(R.layout.home_widzard_one);
        ((Button) findViewById(R.id.next_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HomeWizard.this.gotoDefaultLauncher();
                HomeWizard.this.finish();
            }
        });
    }

    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts(SCHEME, packageName, (String) null));
        } else {
            String appPkgName = apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21;
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        intent.addFlags(268435456);
        try {
            Toast.makeText(context, R.string.home_widzard_one_toast, 1).show();
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ActivityNotFoundException activityNotFoundException = e;
            Toast.makeText(context, R.string.not_access_appdetail, 0).show();
        }
    }

    /* access modifiers changed from: private */
    public void gotoDefaultLauncher() {
        PackageManager localPackageManager = getPackageManager();
        Intent launcherIntent = new Intent("android.intent.action.MAIN");
        launcherIntent.addCategory("android.intent.category.HOME");
        ResolveInfo localResolveInfo = localPackageManager.resolveActivity(launcherIntent, 65536);
        if (localResolveInfo.activityInfo.name.equals("com.android.internal.app.ResolverActivity")) {
            Toast.makeText(this, R.string.home_widzard_only_home, 1).show();
        } else {
            showInstalledAppDetails(this, localResolveInfo.activityInfo.packageName);
        }
    }
}
