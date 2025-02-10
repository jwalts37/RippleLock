package com.mobclick.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONArray;
import org.json.JSONObject;

public class l {
    static String a = "utf-8";
    public static int b;

    public static int a(Context context, String str, String str2) {
        try {
            Field field = Class.forName(String.valueOf(context.getPackageName()) + ".R$" + str).getField(str2);
            return Integer.parseInt(field.get(field.getName()).toString());
        } catch (Exception e) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "reflect resource error");
                e.printStackTrace();
            }
            return 0;
        }
    }

    public static int a(Date date, Date date2) {
        Date date3;
        Date date4;
        if (date.after(date2)) {
            date3 = date;
            date4 = date2;
        } else {
            date3 = date2;
            date4 = date;
        }
        return (int) ((date3.getTime() - date4.getTime()) / 1000);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a() {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x003d }
            java.lang.String r2 = "/proc/cpuinfo"
            r1.<init>(r2)     // Catch:{ FileNotFoundException -> 0x003d }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x002c }
            r3 = 1024(0x400, float:1.435E-42)
            r2.<init>(r1, r3)     // Catch:{ IOException -> 0x002c }
            java.lang.String r0 = r2.readLine()     // Catch:{ IOException -> 0x002c }
            r2.close()     // Catch:{ IOException -> 0x0055, FileNotFoundException -> 0x004e }
            r1.close()     // Catch:{ IOException -> 0x0055, FileNotFoundException -> 0x004e }
        L_0x0019:
            if (r0 == 0) goto L_0x0027
            r1 = 58
            int r1 = r0.indexOf(r1)
            int r1 = r1 + 1
            java.lang.String r0 = r0.substring(r1)
        L_0x0027:
            java.lang.String r0 = r0.trim()
            return r0
        L_0x002c:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0030:
            boolean r2 = com.mobclick.android.UmengConstants.testMode     // Catch:{ FileNotFoundException -> 0x0053 }
            if (r2 == 0) goto L_0x004c
            java.lang.String r2 = "MobclickAgent"
            java.lang.String r3 = "Could not read from file /proc/cpuinfo"
            android.util.Log.e(r2, r3, r0)     // Catch:{ FileNotFoundException -> 0x0053 }
            r0 = r1
            goto L_0x0019
        L_0x003d:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0041:
            boolean r2 = com.mobclick.android.UmengConstants.testMode
            if (r2 == 0) goto L_0x004c
            java.lang.String r2 = "MobclickAgent"
            java.lang.String r3 = "Could not open file /proc/cpuinfo"
            android.util.Log.e(r2, r3, r0)
        L_0x004c:
            r0 = r1
            goto L_0x0019
        L_0x004e:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0041
        L_0x0053:
            r0 = move-exception
            goto L_0x0041
        L_0x0055:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.l.a():java.lang.String");
    }

    public static String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                stringBuffer.append(Integer.toHexString(b2 & 255));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "getMD5 error");
                e.printStackTrace();
            }
            return "";
        }
    }

    public static String a(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String a(JSONObject jSONObject) {
        try {
            if (jSONObject.has("channel")) {
                jSONObject.put("channel", URLEncoder.encode(jSONObject.getString("channel"), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static void a(Context context) {
        Toast.makeText(context, context.getString(a(context, "string", "UMToast_IsUpdating")), 0).show();
    }

    public static void a(Context context, Date date) {
        SharedPreferences.Editor edit = context.getSharedPreferences("exchange_last_request_time", 0).edit();
        edit.putString("last_request_time", a(date));
        edit.commit();
    }

    public static boolean a(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public static String[] a(GL10 gl10) {
        try {
            return new String[]{gl10.glGetString(7936), gl10.glGetString(7937)};
        } catch (Exception e) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Could not read gpu infor:", e);
            }
            return new String[0];
        }
    }

    public static String b() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String b(Context context) {
        String str = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                String string = applicationInfo.metaData.getString("UMENG_APPKEY");
                if (string != null) {
                    str = string;
                } else if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                }
            }
        } catch (Exception e) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                e.printStackTrace();
            }
        }
        return str.trim();
    }

    public static String b(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                byte b3 = b2 & 255;
                if (b3 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(b3));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "getMD5 error");
                e.printStackTrace();
            }
            return "";
        }
    }

    public static String b(JSONObject jSONObject) {
        try {
            JSONObject optJSONObject = jSONObject.optJSONObject("header");
            if (optJSONObject != null) {
                if (optJSONObject.has("access_subtype")) {
                    optJSONObject.put("access_subtype", URLEncoder.encode(optJSONObject.getString("access_subtype"), "UTF-8"));
                }
                if (optJSONObject.has("cpu")) {
                    optJSONObject.put("cpu", URLEncoder.encode(optJSONObject.getString("cpu"), "UTF-8"));
                }
                if (optJSONObject.has(UmengConstants.AtomKey_AppVersion)) {
                    optJSONObject.put(UmengConstants.AtomKey_AppVersion, URLEncoder.encode(optJSONObject.getString(UmengConstants.AtomKey_AppVersion), "UTF-8"));
                }
                if (optJSONObject.has("country")) {
                    optJSONObject.put("country", URLEncoder.encode(optJSONObject.getString("country"), "UTF-8"));
                }
                if (optJSONObject.has(UmengConstants.AtomKey_DeviceModel)) {
                    optJSONObject.put(UmengConstants.AtomKey_DeviceModel, URLEncoder.encode(optJSONObject.getString(UmengConstants.AtomKey_DeviceModel), "UTF-8"));
                }
                if (optJSONObject.has("carrier")) {
                    optJSONObject.put("carrier", URLEncoder.encode(optJSONObject.getString("carrier"), "UTF-8"));
                }
                if (optJSONObject.has("language")) {
                    optJSONObject.put("language", URLEncoder.encode(optJSONObject.getString("language"), "UTF-8"));
                }
                if (optJSONObject.has("channel")) {
                    optJSONObject.put("channel", URLEncoder.encode(optJSONObject.getString("channel"), "UTF-8"));
                }
            }
            JSONObject optJSONObject2 = jSONObject.optJSONObject("body");
            JSONArray optJSONArray = optJSONObject2.optJSONArray("event");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject3 = optJSONArray.optJSONObject(i);
                    if (optJSONObject3 != null) {
                        if (optJSONObject3.has("label")) {
                            optJSONObject3.put("label", URLEncoder.encode(optJSONObject3.getString("label")));
                        }
                        if (optJSONObject3.has("tag")) {
                            optJSONObject3.put("tag", URLEncoder.encode(optJSONObject3.getString("tag")));
                        }
                    }
                }
            }
            JSONArray optJSONArray2 = optJSONObject2.optJSONArray(UmengConstants.Atom_State_Error);
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    JSONObject optJSONObject4 = optJSONArray2.optJSONObject(i2);
                    if (optJSONObject4 != null && optJSONObject4.has("context")) {
                        optJSONObject4.put("context", URLEncoder.encode(optJSONObject4.getString("context")));
                    }
                }
            }
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jSONObject.toString();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String c(android.content.Context r4) {
        /*
            java.lang.String r0 = "phone"
            java.lang.Object r0 = r4.getSystemService(r0)
            android.telephony.TelephonyManager r0 = (android.telephony.TelephonyManager) r0
            if (r0 != 0) goto L_0x0015
            boolean r1 = com.mobclick.android.UmengConstants.testMode
            if (r1 == 0) goto L_0x0015
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r2 = "No IMEI."
            android.util.Log.w(r1, r2)
        L_0x0015:
            java.lang.String r1 = ""
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = a((android.content.Context) r4, (java.lang.String) r2)     // Catch:{ Exception -> 0x0047 }
            if (r2 == 0) goto L_0x0056
            java.lang.String r0 = r0.getDeviceId()     // Catch:{ Exception -> 0x0047 }
        L_0x0023:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x0058
            boolean r0 = com.mobclick.android.UmengConstants.testMode
            if (r0 == 0) goto L_0x0034
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "No IMEI."
            android.util.Log.w(r0, r1)
        L_0x0034:
            java.lang.String r0 = i(r4)
            if (r0 != 0) goto L_0x0058
            boolean r0 = com.mobclick.android.UmengConstants.testMode
            if (r0 == 0) goto L_0x0045
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "Failed to take mac as IMEI."
            android.util.Log.w(r0, r1)
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            r0 = move-exception
            boolean r2 = com.mobclick.android.UmengConstants.testMode
            if (r2 == 0) goto L_0x0056
            java.lang.String r2 = "MobclickAgent"
            java.lang.String r3 = "No IMEI."
            android.util.Log.w(r2, r3)
            r0.printStackTrace()
        L_0x0056:
            r0 = r1
            goto L_0x0023
        L_0x0058:
            java.lang.String r0 = a((java.lang.String) r0)
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.l.c(android.content.Context):java.lang.String");
    }

    public static byte[] c(String str) {
        b = 0;
        Deflater deflater = new Deflater();
        deflater.setInput(str.getBytes(a));
        deflater.finish();
        byte[] bArr = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (!deflater.finished()) {
            int deflate = deflater.deflate(bArr);
            b += deflate;
            byteArrayOutputStream.write(bArr, 0, deflate);
        }
        deflater.end();
        return byteArrayOutputStream.toByteArray();
    }

    public static Date d(Context context) {
        return d(context.getSharedPreferences("exchange_last_request_time", 0).getString("last_request_time", "1900-01-01 00:00:00"));
    }

    public static Date d(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01bc, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x01bf, code lost:
        if (com.mobclick.android.UmengConstants.testMode != false) goto L_0x01c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x01c1, code lost:
        android.util.Log.e(com.mobclick.android.UmengConstants.LOG_TAG, "getMessageHeader error");
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:?, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject e(android.content.Context r6) {
        /*
            r5 = 0
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            java.lang.String r0 = "phone"
            java.lang.Object r0 = r6.getSystemService(r0)     // Catch:{ Exception -> 0x01bc }
            android.telephony.TelephonyManager r0 = (android.telephony.TelephonyManager) r0     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = c((android.content.Context) r6)     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x001c
            java.lang.String r3 = ""
            boolean r3 = r1.equals(r3)     // Catch:{ Exception -> 0x01bc }
            if (r3 == 0) goto L_0x0029
        L_0x001c:
            boolean r0 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x0027
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "No device id"
            android.util.Log.e(r0, r1)     // Catch:{ Exception -> 0x01bc }
        L_0x0027:
            r0 = r5
        L_0x0028:
            return r0
        L_0x0029:
            java.lang.String r3 = "idmd5"
            r2.put(r3, r1)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "device_model"
            java.lang.String r3 = android.os.Build.MODEL     // Catch:{ Exception -> 0x01bc }
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = b((android.content.Context) r6)     // Catch:{ Exception -> 0x01bc }
            if (r1 != 0) goto L_0x0048
            boolean r0 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x0046
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "No appkey"
            android.util.Log.e(r0, r1)     // Catch:{ Exception -> 0x01bc }
        L_0x0046:
            r0 = r5
            goto L_0x0028
        L_0x0048:
            java.lang.String r3 = "appkey"
            r2.put(r3, r1)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = com.mobclick.android.UmengConstants.channel     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x0197
            java.lang.String r1 = com.mobclick.android.UmengConstants.channel     // Catch:{ Exception -> 0x01bc }
        L_0x0053:
            java.lang.String r3 = "channel"
            r2.put(r3, r1)     // Catch:{ Exception -> 0x01bc }
            android.content.pm.PackageManager r1 = r6.getPackageManager()     // Catch:{ NameNotFoundException -> 0x019d }
            java.lang.String r3 = r6.getPackageName()     // Catch:{ NameNotFoundException -> 0x019d }
            r4 = 0
            android.content.pm.PackageInfo r1 = r1.getPackageInfo(r3, r4)     // Catch:{ NameNotFoundException -> 0x019d }
            java.lang.String r3 = r1.versionName     // Catch:{ NameNotFoundException -> 0x019d }
            int r1 = r1.versionCode     // Catch:{ NameNotFoundException -> 0x019d }
            java.lang.String r4 = "app_version"
            r2.put(r4, r3)     // Catch:{ NameNotFoundException -> 0x019d }
            java.lang.String r3 = "version_code"
            r2.put(r3, r1)     // Catch:{ NameNotFoundException -> 0x019d }
        L_0x0073:
            java.lang.String r1 = "sdk_type"
            java.lang.String r3 = "Android"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "sdk_version"
            java.lang.String r3 = "3.1.0"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "os"
            java.lang.String r3 = "Android"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "os_version"
            java.lang.String r3 = android.os.Build.VERSION.RELEASE     // Catch:{ Exception -> 0x01bc }
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            android.content.res.Configuration r1 = new android.content.res.Configuration     // Catch:{ Exception -> 0x01bc }
            r1.<init>()     // Catch:{ Exception -> 0x01bc }
            android.content.ContentResolver r3 = r6.getContentResolver()     // Catch:{ Exception -> 0x01bc }
            android.provider.Settings.System.getConfiguration(r3, r1)     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x01e0
            java.util.Locale r3 = r1.locale     // Catch:{ Exception -> 0x01bc }
            if (r3 == 0) goto L_0x01e0
            java.lang.String r3 = "country"
            java.util.Locale r4 = r1.locale     // Catch:{ Exception -> 0x01bc }
            java.lang.String r4 = r4.getCountry()     // Catch:{ Exception -> 0x01bc }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r3 = "language"
            java.util.Locale r4 = r1.locale     // Catch:{ Exception -> 0x01bc }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x01bc }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x01bc }
            java.util.Locale r1 = r1.locale     // Catch:{ Exception -> 0x01bc }
            java.util.Calendar r1 = java.util.Calendar.getInstance(r1)     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x01d7
            java.util.TimeZone r1 = r1.getTimeZone()     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x01ce
            java.lang.String r3 = "timezone"
            int r1 = r1.getRawOffset()     // Catch:{ Exception -> 0x01bc }
            r4 = 3600000(0x36ee80, float:5.044674E-39)
            int r1 = r1 / r4
            r2.put(r3, r1)     // Catch:{ Exception -> 0x01bc }
        L_0x00d2:
            android.util.DisplayMetrics r3 = new android.util.DisplayMetrics     // Catch:{ Exception -> 0x01f7 }
            r3.<init>()     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r1 = "window"
            java.lang.Object r1 = r6.getSystemService(r1)     // Catch:{ Exception -> 0x01f7 }
            android.view.WindowManager r1 = (android.view.WindowManager) r1     // Catch:{ Exception -> 0x01f7 }
            android.view.Display r1 = r1.getDefaultDisplay()     // Catch:{ Exception -> 0x01f7 }
            r1.getMetrics(r3)     // Catch:{ Exception -> 0x01f7 }
            int r1 = r3.widthPixels     // Catch:{ Exception -> 0x01f7 }
            int r3 = r3.heightPixels     // Catch:{ Exception -> 0x01f7 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x01f7 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r3 = "*"
            java.lang.StringBuilder r3 = r4.append(r3)     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x01f7 }
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x01f7 }
            java.lang.String r3 = "resolution"
            r2.put(r3, r1)     // Catch:{ Exception -> 0x01f7 }
        L_0x010e:
            java.lang.String[] r1 = f(r6)     // Catch:{ Exception -> 0x020f }
            java.lang.String r3 = "access"
            r4 = 0
            r4 = r1[r4]     // Catch:{ Exception -> 0x020f }
            r2.put(r3, r4)     // Catch:{ Exception -> 0x020f }
            r3 = 0
            r3 = r1[r3]     // Catch:{ Exception -> 0x020f }
            java.lang.String r4 = "2G/3G"
            boolean r3 = r3.equals(r4)     // Catch:{ Exception -> 0x020f }
            if (r3 == 0) goto L_0x012d
            java.lang.String r3 = "access_subtype"
            r4 = 1
            r1 = r1[r4]     // Catch:{ Exception -> 0x020f }
            r2.put(r3, r1)     // Catch:{ Exception -> 0x020f }
        L_0x012d:
            java.lang.String r1 = "carrier"
            java.lang.String r0 = r0.getNetworkOperatorName()     // Catch:{ Exception -> 0x0227 }
            r2.put(r1, r0)     // Catch:{ Exception -> 0x0227 }
        L_0x0136:
            boolean r0 = com.mobclick.android.UmengConstants.LOCATION_OPEN     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x015a
            android.location.Location r0 = j(r6)     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x023f
            java.lang.String r1 = "lat"
            double r3 = r0.getLatitude()     // Catch:{ Exception -> 0x01bc }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x01bc }
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "lng"
            double r3 = r0.getLongitude()     // Catch:{ Exception -> 0x01bc }
            java.lang.String r0 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x01bc }
            r2.put(r1, r0)     // Catch:{ Exception -> 0x01bc }
        L_0x015a:
            java.lang.String r0 = a()     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "cpu"
            r2.put(r1, r0)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r0 = com.mobclick.android.MobclickAgent.GPU_VENDER     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x01bc }
            if (r0 != 0) goto L_0x0174
            java.lang.String r0 = "gpu_vender"
            java.lang.String r1 = com.mobclick.android.MobclickAgent.GPU_VENDER     // Catch:{ Exception -> 0x01bc }
            r2.put(r0, r1)     // Catch:{ Exception -> 0x01bc }
        L_0x0174:
            java.lang.String r0 = com.mobclick.android.MobclickAgent.GPU_RENDERER     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x01bc }
            if (r0 != 0) goto L_0x0185
            java.lang.String r0 = "gpu_renderer"
            java.lang.String r1 = com.mobclick.android.MobclickAgent.GPU_RENDERER     // Catch:{ Exception -> 0x01bc }
            r2.put(r0, r1)     // Catch:{ Exception -> 0x01bc }
        L_0x0185:
            boolean r0 = com.mobclick.android.UmengConstants.canCollectionUserInfo     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x0194
            org.json.JSONObject r0 = k(r6)     // Catch:{ Exception -> 0x01bc }
            if (r0 == 0) goto L_0x0194
            java.lang.String r1 = "uinfo"
            r2.put(r1, r0)     // Catch:{ Exception -> 0x01bc }
        L_0x0194:
            r0 = r2
            goto L_0x0028
        L_0x0197:
            java.lang.String r1 = g(r6)     // Catch:{ Exception -> 0x01bc }
            goto L_0x0053
        L_0x019d:
            r1 = move-exception
            boolean r3 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r3 == 0) goto L_0x01ac
            java.lang.String r3 = "MobclickAgent"
            java.lang.String r4 = "read version fail"
            android.util.Log.i(r3, r4)     // Catch:{ Exception -> 0x01bc }
            r1.printStackTrace()     // Catch:{ Exception -> 0x01bc }
        L_0x01ac:
            java.lang.String r1 = "app_version"
            java.lang.String r3 = "unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "version_code"
            java.lang.String r3 = "unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x0073
        L_0x01bc:
            r0 = move-exception
            boolean r1 = com.mobclick.android.UmengConstants.testMode
            if (r1 == 0) goto L_0x01cb
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r2 = "getMessageHeader error"
            android.util.Log.e(r1, r2)
            r0.printStackTrace()
        L_0x01cb:
            r0 = r5
            goto L_0x0028
        L_0x01ce:
            java.lang.String r1 = "timezone"
            r3 = 8
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x00d2
        L_0x01d7:
            java.lang.String r1 = "timezone"
            r3 = 8
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x00d2
        L_0x01e0:
            java.lang.String r1 = "country"
            java.lang.String r3 = "Unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "language"
            java.lang.String r3 = "Unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r1 = "timezone"
            r3 = 8
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x00d2
        L_0x01f7:
            r1 = move-exception
            boolean r3 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r3 == 0) goto L_0x0206
            java.lang.String r3 = "MobclickAgent"
            java.lang.String r4 = "read resolution fail"
            android.util.Log.e(r3, r4)     // Catch:{ Exception -> 0x01bc }
            r1.printStackTrace()     // Catch:{ Exception -> 0x01bc }
        L_0x0206:
            java.lang.String r1 = "resolution"
            java.lang.String r3 = "Unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x010e
        L_0x020f:
            r1 = move-exception
            boolean r3 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r3 == 0) goto L_0x021e
            java.lang.String r3 = "MobclickAgent"
            java.lang.String r4 = "read access fail"
            android.util.Log.i(r3, r4)     // Catch:{ Exception -> 0x01bc }
            r1.printStackTrace()     // Catch:{ Exception -> 0x01bc }
        L_0x021e:
            java.lang.String r1 = "access"
            java.lang.String r3 = "Unknown"
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x012d
        L_0x0227:
            r0 = move-exception
            boolean r1 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x01bc }
            if (r1 == 0) goto L_0x0236
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r3 = "read carrier fail"
            android.util.Log.i(r1, r3)     // Catch:{ Exception -> 0x01bc }
            r0.printStackTrace()     // Catch:{ Exception -> 0x01bc }
        L_0x0236:
            java.lang.String r0 = "carrier"
            java.lang.String r1 = "Unknown"
            r2.put(r0, r1)     // Catch:{ Exception -> 0x01bc }
            goto L_0x0136
        L_0x023f:
            java.lang.String r0 = "lat"
            r3 = 0
            r2.put(r0, r3)     // Catch:{ Exception -> 0x01bc }
            java.lang.String r0 = "lng"
            r3 = 0
            r2.put(r0, r3)     // Catch:{ Exception -> 0x01bc }
            goto L_0x015a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.l.e(android.content.Context):org.json.JSONObject");
    }

    public static String[] f(Context context) {
        String[] strArr = {"Unknown", "Unknown"};
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            strArr[0] = "Unknown";
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                strArr[0] = "Unknown";
            } else if (connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
                strArr[0] = "Wi-Fi";
            } else {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    strArr[0] = "2G/3G";
                    strArr[1] = networkInfo.getSubtypeName();
                }
            }
        }
        return strArr;
    }

    public static String g(Context context) {
        Object obj;
        if (UmengConstants.channel != null) {
            return UmengConstants.channel;
        }
        String str = "Unknown";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null || (obj = applicationInfo.metaData.get("UMENG_CHANNEL")) == null)) {
                String obj2 = obj.toString();
                if (obj2 != null) {
                    str = obj2;
                } else if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "Could not read UMENG_CHANNEL meta-data from AndroidManifest.xml.");
                }
            }
        } catch (Exception e) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "Could not read UMENG_CHANNEL meta-data from AndroidManifest.xml.");
                e.printStackTrace();
            }
        }
        UmengConstants.channel = str;
        return str;
    }

    public static boolean h(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        if (connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED;
    }

    private static String i(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            if (!UmengConstants.testMode) {
                return null;
            }
            Log.i(UmengConstants.LOG_TAG, "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?", e);
            return null;
        }
    }

    private static Location j(Context context) {
        if (!MobclickAgent.mUseLocationService) {
            return null;
        }
        return new e(context).a();
    }

    private static JSONObject k(Context context) {
        JSONObject jSONObject = new JSONObject();
        SharedPreferences a2 = MobclickAgent.a(context);
        try {
            if (a2.getInt(UmengConstants.AtomKey_Sex, -1) != -1) {
                jSONObject.put(UmengConstants.TrivialPreKey_Sex, a2.getInt(UmengConstants.AtomKey_Sex, -1));
            }
            if (a2.getInt("age", -1) != -1) {
                jSONObject.put("age", a2.getInt("age", -1));
            }
            if (!"".equals(a2.getString(UmengConstants.AtomKey_User_ID, ""))) {
                jSONObject.put("id", a2.getString(UmengConstants.AtomKey_User_ID, ""));
            }
            if (jSONObject.length() > 0) {
                return jSONObject;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
