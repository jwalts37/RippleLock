package com.nanoha.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    public static final boolean DEBUG = true;
    private static CrashHandler INSTANCE = null;
    private static final String STACK_TRACE = "STACK_TRACE";
    public static final String TAG = "CrashHandler";
    private static final String VERSION_CODE = "versionCode";
    private static final String VERSION_NAME = "versionName";
    /* access modifiers changed from: private */
    public Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Properties mDeviceCrashInfo = new Properties();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        this.mContext = ctx;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(10);
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        String localizedMessage = ex.getLocalizedMessage();
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, CrashHandler.this.mContext.getString(R.string.error_notify), 1).show();
                Looper.loop();
            }
        }.start();
        collectCrashDeviceInfo(this.mContext);
        String saveCrashInfoToFile = saveCrashInfoToFile(ex);
        return true;
    }

    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                this.mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                this.mDeviceCrashInfo.put(VERSION_CODE, Integer.valueOf(pi.versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.mDeviceCrashInfo.put(field.getName(), field.get((Object) null));
                Log.d(TAG, String.valueOf(field.getName()) + " : " + field.get((Object) null));
            } catch (Exception e2) {
                Log.e(TAG, "Error while collect crash info", e2);
            }
        }
    }

    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        String result = info.toString();
        printWriter.close();
        this.mDeviceCrashInfo.put(STACK_TRACE, result);
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/ripplelock" + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = new FileOutputStream(new File(fileName), true);
            trace.write(result.getBytes());
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
            return null;
        }
    }

    private void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            Iterator<String> it = sortedFiles.iterator();
            while (it.hasNext()) {
                File cr = new File(ctx.getFilesDir(), it.next());
                postReport(cr);
                cr.delete();
            }
        }
    }

    private String[] getCrashReportFiles(Context ctx) {
        return ctx.getFilesDir().list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(CrashHandler.CRASH_REPORTER_EXTENSION);
            }
        });
    }

    private void postReport(File file) {
    }

    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(this.mContext);
    }
}
