package com.mobclick.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.nanoha.util.Util;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.microedition.khronos.opengles.GL10;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobclickAgent {
    public static String GPU_RENDERER = "";
    public static String GPU_VENDER = "";
    /* access modifiers changed from: private */
    public static final MobclickAgent a = new MobclickAgent();
    private static int b = 1;
    private static final int e = 0;
    private static final int f = 1;
    private static final int g = 2;
    private static final int h = 3;
    private static UmengUpdateListener i = null;
    private static UmengOnlineConfigureListener j = null;
    private static JSONObject k = null;
    private static /* synthetic */ int[] l;
    public static boolean mUpdateOnlyWifi = true;
    public static boolean mUseLocationService = true;
    public static boolean updateAutoPopup = true;
    private Context c;
    private final Handler d;

    private MobclickAgent() {
        HandlerThread handlerThread = new HandlerThread(UmengConstants.LOG_TAG);
        handlerThread.start();
        this.d = new Handler(handlerThread.getLooper());
    }

    static SharedPreferences a(Context context) {
        return context.getSharedPreferences("mobclick_agent_user_" + context.getPackageName(), 0);
    }

    private String a(Context context, SharedPreferences sharedPreferences) {
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("start_millis", valueOf.longValue());
        edit.putLong("end_millis", -1);
        edit.commit();
        return sharedPreferences.getString("session_id", (String) null);
    }

    private String a(Context context, String str, long j2) {
        StringBuilder sb = new StringBuilder();
        sb.append(j2).append(str).append(l.c(context));
        return sb.toString();
    }

    private String a(Context context, String str, SharedPreferences sharedPreferences) {
        c(context, sharedPreferences);
        long currentTimeMillis = System.currentTimeMillis();
        String a2 = a(context, str, currentTimeMillis);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(UmengConstants.AtomKey_AppKey, str);
        edit.putString("session_id", a2);
        edit.putLong("start_millis", currentTimeMillis);
        edit.putLong("end_millis", -1);
        edit.putLong("duration", 0);
        edit.putString("activities", "");
        edit.commit();
        b(context, sharedPreferences);
        return a2;
    }

    private static String a(Context context, JSONObject jSONObject, String str, boolean z, String str2) {
        HttpPost httpPost = new HttpPost(str);
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        try {
            String a2 = m.a(context);
            if (a2 != null) {
                defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(a2, 80));
            }
            String jSONObject2 = jSONObject.toString();
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, jSONObject2);
            }
            if (!UmengConstants.COMPRESS_DATA || z) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair(UmengConstants.AtomKey_Content, jSONObject2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            } else {
                byte[] c2 = l.c("content=" + jSONObject2);
                httpPost.addHeader("Content-Encoding", "deflate");
                httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(c2), (long) l.b));
            }
            SharedPreferences.Editor edit = h(context).edit();
            Date date = new Date();
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            long time = new Date().getTime() - date.getTime();
            if (execute.getStatusLine().getStatusCode() == 200) {
                if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "Sent message to " + str);
                }
                edit.putLong("req_time", time);
                edit.commit();
                HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    return a(entity.getContent());
                }
                return null;
            }
            edit.putLong("req_time", -1);
            return null;
        } catch (ClientProtocolException e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "ClientProtocolException,Failed to send message.", e2);
                e2.printStackTrace();
            }
            return null;
        } catch (IOException e3) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "IOException,Failed to send message.", e3);
                e3.printStackTrace();
            }
            return null;
        }
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    try {
                        inputStream.close();
                        return sb.toString();
                    } catch (IOException e2) {
                        if (UmengConstants.testMode) {
                            Log.e(UmengConstants.LOG_TAG, "Caught IOException in convertStreamToString()", e2);
                            e2.printStackTrace();
                        }
                        return null;
                    }
                } else {
                    sb.append(String.valueOf(readLine) + "\n");
                }
            } catch (IOException e3) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "Caught IOException in convertStreamToString()", e3);
                    e3.printStackTrace();
                }
                try {
                    inputStream.close();
                    return null;
                } catch (IOException e4) {
                    if (UmengConstants.testMode) {
                        Log.e(UmengConstants.LOG_TAG, "Caught IOException in convertStreamToString()", e4);
                        e4.printStackTrace();
                    }
                    return null;
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                    throw th;
                } catch (IOException e5) {
                    if (UmengConstants.testMode) {
                        Log.e(UmengConstants.LOG_TAG, "Caught IOException in convertStreamToString()", e5);
                        e5.printStackTrace();
                    }
                    return null;
                }
            }
        }
    }

    private JSONArray a(JSONObject jSONObject, JSONArray jSONArray) {
        boolean z;
        try {
            String string = jSONObject.getString("tag");
            String string2 = jSONObject.has("label") ? jSONObject.getString("label") : null;
            String string3 = jSONObject.getString("date");
            int length = jSONArray.length() - 1;
            while (true) {
                if (length < 0) {
                    z = false;
                    break;
                }
                JSONObject jSONObject2 = (JSONObject) jSONArray.get(length);
                if (string2 == null && !jSONObject2.has("label")) {
                    if (string.equals(jSONObject2.get("tag")) && string3.equals(jSONObject2.get("date"))) {
                        jSONObject2.put("acc", jSONObject2.getInt("acc") + 1);
                        z = true;
                        break;
                    }
                } else if (string2 != null && jSONObject2.has("label") && string.equals(jSONObject2.get("tag")) && string2.equals(jSONObject2.get("label")) && string3.equals(jSONObject2.get("date"))) {
                    jSONObject2.put("acc", jSONObject2.getInt("acc") + 1);
                    z = true;
                    break;
                }
                length--;
            }
            if (!z) {
                jSONArray.put(jSONObject);
            }
            return jSONArray;
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "custom log merge error in tryToSendMessage");
                e2.printStackTrace();
            }
            jSONArray.put(jSONObject);
            return jSONArray;
        }
    }

    private static void a(Context context, int i2) {
        if (i2 >= 0 && i2 <= 5) {
            SharedPreferences m = m(context);
            synchronized (UmengConstants.saveOnlineConfigMutex) {
                m.edit().putInt(UmengConstants.Online_Config_Local_Policy, i2).commit();
            }
        } else if (UmengConstants.testMode) {
            Log.e(UmengConstants.LOG_TAG, "Illegal value of report policy");
        }
    }

    private void a(Context context, SharedPreferences sharedPreferences, String str, String str2, int i2) {
        String string = sharedPreferences.getString("session_id", "");
        String c2 = c();
        String str3 = c2.split(" ")[0];
        String str4 = c2.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "event");
            jSONObject.put("session_id", string);
            jSONObject.put("date", str3);
            jSONObject.put("time", str4);
            jSONObject.put("tag", str);
            if (str2 != null) {
                jSONObject.put("label", str2);
            }
            jSONObject.put("acc", i2);
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "json error in emitCustomLogReport");
                e2.printStackTrace();
            }
        }
    }

    private static void a(Context context, Gender gender) {
        int i2;
        SharedPreferences a2 = a(context);
        switch (b()[gender.ordinal()]) {
            case 1:
                i2 = 1;
                break;
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 0;
                break;
            default:
                i2 = 0;
                break;
        }
        a2.edit().putInt(UmengConstants.AtomKey_Sex, i2).commit();
    }

    private synchronized void a(Context context, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "update");
            jSONObject.put(UmengConstants.AtomKey_AppKey, str);
            int i2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String packageName = context.getPackageName();
            jSONObject.put(Util.KEY_VERSION_CODE, i2);
            jSONObject.put("package", packageName);
            jSONObject.put(UmengConstants.AtomKey_SDK_Version, UmengConstants.SDK_VERSION);
            jSONObject.put("idmd5", l.c(context));
            jSONObject.put("channel", l.g(context));
            this.d.post(new k(this, context, jSONObject));
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "exception in updateInternal");
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void a(Context context, String str, String str2) {
        this.c = context;
        SharedPreferences j2 = j(context);
        if (j2 != null) {
            if (a(j2)) {
                String a2 = a(context, str, j2);
                if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "Start new session: " + a2);
                }
            } else {
                String a3 = a(context, j2);
                if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "Extend current session: " + a3);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void a(Context context, String str, String str2, String str3, int i2) {
        SharedPreferences j2 = j(context);
        if (j2 != null) {
            a(context, j2, str2, str3, i2);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        r0 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(android.content.Context r6, org.json.JSONObject r7) {
        /*
            r5 = this;
            java.lang.String r0 = "update"
            boolean r0 = a((java.lang.String) r0, (android.content.Context) r6)
            if (r0 == 0) goto L_0x0065
            boolean r0 = com.mobclick.android.UmengConstants.testMode
            if (r0 == 0) goto L_0x0013
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "start to check update info ..."
            android.util.Log.i(r0, r1)
        L_0x0013:
            r0 = 0
            r1 = 0
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0018:
            java.lang.String[] r2 = com.mobclick.android.UmengConstants.UPDATE_URL_LIST
            int r2 = r2.length
            if (r0 < r2) goto L_0x0024
        L_0x001d:
            r0 = r1
        L_0x001e:
            if (r0 == 0) goto L_0x004e
            r5.d((android.content.Context) r6, (java.lang.String) r0)
        L_0x0023:
            return
        L_0x0024:
            java.lang.String[] r1 = com.mobclick.android.UmengConstants.UPDATE_URL_LIST
            r1 = r1[r0]
            r2 = 1
            java.lang.String r3 = "update"
            java.lang.String r1 = a((android.content.Context) r6, (org.json.JSONObject) r7, (java.lang.String) r1, (boolean) r2, (java.lang.String) r3)
            if (r1 == 0) goto L_0x004b
            boolean r0 = com.mobclick.android.UmengConstants.testMode
            if (r0 == 0) goto L_0x001d
            java.lang.String r0 = "MobclickAgent"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "get update info succeed: "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            android.util.Log.i(r0, r2)
            r0 = r1
            goto L_0x001e
        L_0x004b:
            int r0 = r0 + 1
            goto L_0x0018
        L_0x004e:
            boolean r0 = com.mobclick.android.UmengConstants.testMode
            if (r0 == 0) goto L_0x0059
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "get update info failed"
            android.util.Log.i(r0, r1)
        L_0x0059:
            com.mobclick.android.UmengUpdateListener r0 = i
            if (r0 == 0) goto L_0x0023
            com.mobclick.android.UmengUpdateListener r0 = i
            int r1 = com.mobclick.android.UpdateStatus.Timeout
            r0.onUpdateReturned(r1)
            goto L_0x0023
        L_0x0065:
            com.mobclick.android.UmengUpdateListener r0 = i
            if (r0 == 0) goto L_0x0023
            com.mobclick.android.UmengUpdateListener r0 = i
            int r1 = com.mobclick.android.UpdateStatus.No
            r0.onUpdateReturned(r1)
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.MobclickAgent.a(android.content.Context, org.json.JSONObject):void");
    }

    private boolean a(SharedPreferences sharedPreferences) {
        return System.currentTimeMillis() - sharedPreferences.getLong("end_millis", -1) > UmengConstants.kContinueSessionMillis;
    }

    private static boolean a(String str, Context context) {
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0 && !l.h(context)) {
            return false;
        }
        if (str == "update" || str == "online_config") {
            return true;
        }
        b = n(context);
        if (b == 3) {
            if (str == "flush") {
                return true;
            }
        } else if (str == UmengConstants.Atom_State_Error) {
            return true;
        } else {
            if (b == 1 && str == "launch") {
                return true;
            }
            if (b == 2 && str == "terminate") {
                return true;
            }
            if (b == 0) {
                return true;
            }
            if (b == 4) {
                return !i(context).getString(l.b(), "false").equals("true") && str.equals("launch");
            }
            if (b == 5) {
                return l.f(context)[0].equals("Wi-Fi");
            }
        }
        return false;
    }

    private static AlertDialog b(Context context, File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(l.a(context, "string", "UMUpdateTitle"))).setMessage(context.getString(l.a(context, "string", "UMDialog_InstallAPK"))).setCancelable(false).setPositiveButton(context.getString(l.a(context, "string", "UMUpdateNow")), new f(context, file)).setNegativeButton(context.getString(l.a(context, "string", "UMNotNow")), new g());
        AlertDialog create = builder.create();
        create.setCancelable(true);
        return create;
    }

    private static AlertDialog b(Context context, JSONObject jSONObject) {
        try {
            String string = jSONObject.has("version") ? jSONObject.getString("version") : "";
            String string2 = jSONObject.has("update_log") ? jSONObject.getString("update_log") : "";
            String string3 = jSONObject.has("path") ? jSONObject.getString("path") : "";
            String str = "";
            if (!l.f(context)[0].equals("Wi-Fi")) {
                str = String.valueOf(context.getString(l.a(context, "string", "UMGprsCondition"))) + "\n";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(l.a(context, "string", "UMUpdateTitle"))).setMessage(String.valueOf(str) + context.getString(l.a(context, "string", "UMNewVersion")) + string + "\n" + string2).setCancelable(false).setPositiveButton(context.getString(l.a(context, "string", "UMUpdateNow")), new h(context, string3, string)).setNegativeButton(context.getString(l.a(context, "string", "UMNotNow")), new i());
            AlertDialog create = builder.create();
            create.setCancelable(true);
            return create;
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Fail to create update dialog box.", e2);
                e2.printStackTrace();
            }
            return null;
        }
    }

    private static String b(Context context) {
        String str = "";
        try {
            String packageName = context.getPackageName();
            ArrayList arrayList = new ArrayList();
            arrayList.add("logcat");
            arrayList.add("-d");
            arrayList.add("-v");
            arrayList.add("raw");
            arrayList.add("-s");
            arrayList.add("AndroidRuntime:E");
            arrayList.add("-p");
            arrayList.add(packageName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[]) arrayList.toArray(new String[arrayList.size()])).getInputStream()), 1024);
            boolean z = false;
            String str2 = "";
            boolean z2 = false;
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                if (readLine.indexOf("thread attach failed") < 0) {
                    str2 = String.valueOf(str2) + readLine + 10;
                }
                if (!z2 && readLine.toLowerCase().indexOf("exception") >= 0) {
                    z2 = true;
                }
                z = (z || readLine.indexOf(packageName) < 0) ? z : true;
            }
            if (str2.length() > 0 && z2 && z) {
                str = str2;
            }
            try {
                Runtime.getRuntime().exec("logcat -c");
                return str;
            } catch (Exception e2) {
                if (!UmengConstants.testMode) {
                    return str;
                }
                Log.e(UmengConstants.LOG_TAG, "Failed to clear log in exec(logcat -c)");
                e2.printStackTrace();
                return str;
            }
        } catch (Exception e3) {
            Exception exc = e3;
            String str3 = str;
            Exception exc2 = exc;
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Failed to catch error log in catchLogError");
                exc2.printStackTrace();
            }
            return str3;
        }
    }

    private static void b(Context context, int i2) {
        SharedPreferences a2 = a(context);
        if (i2 >= 0 && i2 <= 200) {
            a2.edit().putInt("age", i2).commit();
        } else if (UmengConstants.testMode) {
            Log.i(UmengConstants.LOG_TAG, "not a valid age!");
        }
    }

    private void b(Context context, SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("session_id", (String) null);
        if (string != null) {
            String c2 = c();
            String str = c2.split(" ")[0];
            String str2 = c2.split(" ")[1];
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(UmengConstants.AtomKey_Type, "launch");
                jSONObject.put("session_id", string);
                jSONObject.put("date", str);
                jSONObject.put("time", str2);
                this.d.post(new k(this, context, jSONObject));
            } catch (JSONException e2) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "json error in emitNewSessionReport");
                    e2.printStackTrace();
                }
            }
        } else if (UmengConstants.testMode) {
            Log.i(UmengConstants.LOG_TAG, "Missing session_id, ignore message");
        }
    }

    /* access modifiers changed from: private */
    public synchronized void b(Context context, String str) {
        String b2 = b(context);
        if (b2 != "" && b2.length() <= 10240) {
            c(context, b2);
        }
    }

    static /* synthetic */ int[] b() {
        int[] iArr = l;
        if (iArr == null) {
            iArr = new int[Gender.values().length];
            try {
                iArr[Gender.Female.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Gender.Male.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Gender.Unknown.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            l = iArr;
        }
        return iArr;
    }

    private static String c() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /* access modifiers changed from: private */
    public synchronized void c(Context context) {
        if (this.c == context) {
            this.c = context;
            SharedPreferences j2 = j(context);
            if (j2 != null) {
                long j3 = j2.getLong("start_millis", -1);
                if (j3 != -1) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long j4 = currentTimeMillis - j3;
                    long j5 = j2.getLong("duration", 0);
                    SharedPreferences.Editor edit = j2.edit();
                    if (UmengConstants.ACTIVITY_DURATION_OPEN) {
                        String string = j2.getString("activities", "");
                        String name = context.getClass().getName();
                        if (!"".equals(string)) {
                            string = String.valueOf(string) + ";";
                        }
                        edit.remove("activities");
                        edit.putString("activities", String.valueOf(string) + "[" + name + "," + (j4 / 1000) + "]");
                    }
                    edit.putLong("start_millis", -1);
                    edit.putLong("end_millis", currentTimeMillis);
                    edit.putLong("duration", j4 + j5);
                    edit.commit();
                } else if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "onEndSession called before onStartSession");
                }
            }
        } else if (UmengConstants.testMode) {
            Log.e(UmengConstants.LOG_TAG, "onPause() called without context from corresponding onResume()");
        }
    }

    private void c(Context context, SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("session_id", (String) null);
        if (string != null) {
            Long valueOf = Long.valueOf(sharedPreferences.getLong("duration", -1));
            if (valueOf.longValue() <= 0) {
                valueOf = 0L;
            }
            String c2 = c();
            String str = c2.split(" ")[0];
            String str2 = c2.split(" ")[1];
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(UmengConstants.AtomKey_Type, "terminate");
                jSONObject.put("session_id", string);
                jSONObject.put("date", str);
                jSONObject.put("time", str2);
                jSONObject.put("duration", String.valueOf(valueOf.longValue() / 1000));
                if (UmengConstants.ACTIVITY_DURATION_OPEN) {
                    String string2 = sharedPreferences.getString("activities", "");
                    if (!"".equals(string2)) {
                        String[] split = string2.split(";");
                        JSONArray jSONArray = new JSONArray();
                        for (String jSONArray2 : split) {
                            jSONArray.put(new JSONArray(jSONArray2));
                        }
                        jSONObject.put("activities", jSONArray);
                    }
                }
                this.d.post(new k(this, context, jSONObject));
            } catch (JSONException e2) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "json error in emitLastEndSessionReport");
                    e2.printStackTrace();
                }
            }
        } else if (UmengConstants.testMode) {
            Log.w(UmengConstants.LOG_TAG, "Missing session_id, ignore message in emitLastEndSessionReport");
        }
    }

    /* access modifiers changed from: private */
    public static void c(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void c(Context context, String str) {
        String c2 = c();
        String str2 = c2.split(" ")[0];
        String str3 = c2.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, UmengConstants.Atom_State_Error);
            jSONObject.put("context", str);
            jSONObject.put("date", str2);
            jSONObject.put("time", str3);
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "json error in emitErrorReport");
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void c(Context context, JSONObject jSONObject) {
        String str;
        SharedPreferences h2 = h(context);
        JSONObject e2 = l.e(context);
        long j2 = h2.getLong("req_time", 0);
        if (j2 != 0) {
            try {
                e2.put("req_time", j2);
            } catch (JSONException e3) {
                if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "json error in tryToSendMessage");
                    e3.printStackTrace();
                }
            }
        }
        h2.edit().putString("header", e2.toString()).commit();
        JSONObject f2 = f(context);
        JSONObject jSONObject2 = new JSONObject();
        try {
            String string = jSONObject.getString(UmengConstants.AtomKey_Type);
            if (string != null) {
                if (string != "flush") {
                    jSONObject.remove(UmengConstants.AtomKey_Type);
                    if (f2 == null) {
                        f2 = new JSONObject();
                        JSONArray jSONArray = new JSONArray();
                        jSONArray.put(jSONObject);
                        f2.put(string, jSONArray);
                    } else if (f2.isNull(string)) {
                        JSONArray jSONArray2 = new JSONArray();
                        jSONArray2.put(jSONObject);
                        f2.put(string, jSONArray2);
                    } else {
                        f2.getJSONArray(string).put(jSONObject);
                    }
                }
                if (f2 != null) {
                    jSONObject2.put("header", e2);
                    jSONObject2.put("body", f2);
                    if (a(string, context)) {
                        String str2 = null;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= UmengConstants.APPLOG_URL_LIST.length) {
                                str = str2;
                                break;
                            }
                            str2 = a(context, jSONObject2, UmengConstants.APPLOG_URL_LIST[i2], false, string);
                            if (str2 != null) {
                                str = str2;
                                break;
                            }
                            i2++;
                        }
                        if (str != null) {
                            if (UmengConstants.testMode) {
                                Log.i(UmengConstants.LOG_TAG, "send applog succeed :" + str);
                            }
                            g(context);
                            if (b == 4) {
                                SharedPreferences.Editor edit = i(context).edit();
                                edit.putString(l.b(), "true");
                                edit.commit();
                                return;
                            }
                            return;
                        } else if (UmengConstants.testMode) {
                            Log.i(UmengConstants.LOG_TAG, "send applog failed");
                        }
                    }
                    d(context, f2);
                } else if (UmengConstants.testMode) {
                    Log.w(UmengConstants.LOG_TAG, "No cache message to flush in tryToSendMessage");
                }
            }
        } catch (JSONException e4) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Fail to construct json message in tryToSendMessage.");
                e4.printStackTrace();
            }
            g(context);
        }
    }

    private synchronized void d(Context context) {
        e(context);
    }

    private void d(Context context, String str) {
        try {
            k = new JSONObject(str);
            if (k.getString("update").equals("Yes")) {
                if (i != null) {
                    i.onUpdateReturned(UpdateStatus.Yes);
                }
                if (updateAutoPopup) {
                    showUpdateDialog(context);
                }
            } else if (i != null) {
                i.onUpdateReturned(UpdateStatus.No);
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "show update dialog error");
                e2.printStackTrace();
            }
        }
    }

    private static void d(Context context, JSONObject jSONObject) {
        try {
            FileOutputStream openFileOutput = context.openFileOutput(l(context), 0);
            openFileOutput.write(jSONObject.toString().getBytes());
            openFileOutput.close();
        } catch (FileNotFoundException e2) {
            if (UmengConstants.testMode) {
                e2.printStackTrace();
            }
        } catch (IOException e3) {
            if (UmengConstants.testMode) {
                e3.printStackTrace();
            }
        }
    }

    private static File e(Context context, JSONObject jSONObject) {
        String absolutePath;
        try {
            String string = jSONObject.has("path") ? jSONObject.getString("path") : "";
            String string2 = jSONObject.has("version") ? jSONObject.getString("version") : "";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(string).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            int contentLength = httpURLConnection.getContentLength();
            httpURLConnection.disconnect();
            if (Environment.getExternalStorageState().equals("mounted")) {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                absolutePath = String.valueOf(externalStorageDirectory.getParent()) + "/" + externalStorageDirectory.getName() + "/download";
            } else {
                absolutePath = context.getFilesDir().getAbsolutePath();
            }
            File file = new File(absolutePath, a.a(context.getPackageName(), string2, contentLength));
            if (file.exists()) {
                if (file.length() == ((long) contentLength)) {
                    return file;
                }
                if (UmengConstants.canResume) {
                    return file;
                }
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "Something happended in checking existing version , you may ignore this");
            }
        }
        return null;
    }

    private void e(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "flush");
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "json error in emitCache");
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void e(android.content.Context r6, java.lang.String r7) {
        /*
            android.content.SharedPreferences r0 = m(r6)
            java.lang.Object r1 = com.mobclick.android.UmengConstants.saveOnlineConfigMutex
            monitor-enter(r1)
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0085 }
            r2.<init>(r7)     // Catch:{ Exception -> 0x0085 }
            java.lang.String r3 = "last_config_time"
            boolean r3 = r2.has(r3)     // Catch:{ Exception -> 0x0096 }
            if (r3 == 0) goto L_0x0027
            android.content.SharedPreferences$Editor r3 = r0.edit()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r4 = "umeng_last_config_time"
            java.lang.String r5 = "last_config_time"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Exception -> 0x0096 }
            android.content.SharedPreferences$Editor r3 = r3.putString(r4, r5)     // Catch:{ Exception -> 0x0096 }
            r3.commit()     // Catch:{ Exception -> 0x0096 }
        L_0x0027:
            java.lang.String r3 = "report_policy"
            boolean r3 = r2.has(r3)     // Catch:{ Exception -> 0x009f }
            if (r3 == 0) goto L_0x0042
            android.content.SharedPreferences$Editor r3 = r0.edit()     // Catch:{ Exception -> 0x009f }
            java.lang.String r4 = "umeng_net_report_policy"
            java.lang.String r5 = "report_policy"
            int r5 = r2.getInt(r5)     // Catch:{ Exception -> 0x009f }
            android.content.SharedPreferences$Editor r3 = r3.putInt(r4, r5)     // Catch:{ Exception -> 0x009f }
            r3.commit()     // Catch:{ Exception -> 0x009f }
        L_0x0042:
            java.lang.String r3 = "online_params"
            boolean r3 = r2.has(r3)     // Catch:{ Exception -> 0x00b6 }
            if (r3 == 0) goto L_0x0083
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = "online_params"
            java.lang.String r2 = r2.getString(r4)     // Catch:{ Exception -> 0x00b6 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00b6 }
            java.util.Iterator r2 = r3.keys()     // Catch:{ Exception -> 0x00b6 }
            android.content.SharedPreferences$Editor r0 = r0.edit()     // Catch:{ Exception -> 0x00b6 }
        L_0x005d:
            boolean r4 = r2.hasNext()     // Catch:{ Exception -> 0x00b6 }
            if (r4 != 0) goto L_0x00a8
            r0.commit()     // Catch:{ Exception -> 0x00b6 }
            com.mobclick.android.UmengOnlineConfigureListener r0 = j     // Catch:{ Exception -> 0x00b6 }
            if (r0 == 0) goto L_0x006f
            com.mobclick.android.UmengOnlineConfigureListener r0 = j     // Catch:{ Exception -> 0x00b6 }
            r0.onDataReceived(r3)     // Catch:{ Exception -> 0x00b6 }
        L_0x006f:
            java.lang.String r0 = "MobclickAgent"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = "get online setting params: "
            r2.<init>(r4)     // Catch:{ Exception -> 0x00b6 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00b6 }
            android.util.Log.i(r0, r2)     // Catch:{ Exception -> 0x00b6 }
        L_0x0083:
            monitor-exit(r1)     // Catch:{ all -> 0x0093 }
        L_0x0084:
            return
        L_0x0085:
            r0 = move-exception
            boolean r0 = com.mobclick.android.UmengConstants.testMode     // Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x0091
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r2 = "not json string"
            android.util.Log.i(r0, r2)     // Catch:{ all -> 0x0093 }
        L_0x0091:
            monitor-exit(r1)     // Catch:{ all -> 0x0093 }
            goto L_0x0084
        L_0x0093:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0093 }
            throw r0
        L_0x0096:
            r3 = move-exception
            boolean r4 = com.mobclick.android.UmengConstants.testMode     // Catch:{ all -> 0x0093 }
            if (r4 == 0) goto L_0x0027
            r3.printStackTrace()     // Catch:{ all -> 0x0093 }
            goto L_0x0027
        L_0x009f:
            r3 = move-exception
            boolean r4 = com.mobclick.android.UmengConstants.testMode     // Catch:{ all -> 0x0093 }
            if (r4 == 0) goto L_0x0042
            r3.printStackTrace()     // Catch:{ all -> 0x0093 }
            goto L_0x0042
        L_0x00a8:
            java.lang.Object r6 = r2.next()     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = r3.getString(r6)     // Catch:{ Exception -> 0x00b6 }
            r0.putString(r6, r4)     // Catch:{ Exception -> 0x00b6 }
            goto L_0x005d
        L_0x00b6:
            r0 = move-exception
            boolean r2 = com.mobclick.android.UmengConstants.testMode     // Catch:{ all -> 0x0093 }
            if (r2 == 0) goto L_0x0083
            r0.printStackTrace()     // Catch:{ all -> 0x0093 }
            goto L_0x0083
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.MobclickAgent.e(android.content.Context, java.lang.String):void");
    }

    public static void enterPage(Context context, String str) {
        onEvent(context, "_PAGE_", str);
    }

    private static JSONObject f(Context context) {
        try {
            FileInputStream openFileInput = context.openFileInput(l(context));
            String str = "";
            byte[] bArr = new byte[16384];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                str = String.valueOf(str) + new String(bArr, 0, read);
            }
            if (str.length() == 0) {
                return null;
            }
            try {
                return new JSONObject(str);
            } catch (JSONException e2) {
                openFileInput.close();
                g(context);
                e2.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e3) {
            return null;
        } catch (IOException e4) {
            return null;
        }
    }

    private synchronized void f(Context context, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "online_config");
            jSONObject.put(UmengConstants.AtomKey_AppKey, str);
            int i2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String packageName = context.getPackageName();
            jSONObject.put(Util.KEY_VERSION_CODE, i2);
            jSONObject.put("package", packageName);
            jSONObject.put(UmengConstants.AtomKey_SDK_Version, UmengConstants.SDK_VERSION);
            jSONObject.put("idmd5", l.c(context));
            jSONObject.put("channel", l.g(context));
            jSONObject.put("report_policy", n(context));
            jSONObject.put("last_config_time", o(context));
            this.d.post(new k(this, context, jSONObject));
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "exception in onlineConfigInternal");
            }
        }
    }

    /* access modifiers changed from: private */
    public void f(Context context, JSONObject jSONObject) {
        if (a("online_config", context)) {
            if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "start to check onlineConfig info ...");
            }
            String a2 = a(context, jSONObject, UmengConstants.CONFIG_URL, true, "online_config");
            if (a2 == null) {
                a2 = a(context, jSONObject, UmengConstants.CONFIG_URL_BACK, true, "online_config");
            }
            if (a2 != null) {
                if (UmengConstants.testMode) {
                    Log.i(UmengConstants.LOG_TAG, "get onlineConfig info succeed !");
                }
                e(context, a2);
            } else if (UmengConstants.testMode) {
                Log.i(UmengConstants.LOG_TAG, "get onlineConfig info failed !");
            }
        }
    }

    public static void flush(Context context) {
        if (context == null) {
            try {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected null context in flush");
                }
            } catch (Exception e2) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.flush(). ");
                    e2.printStackTrace();
                    return;
                }
                return;
            }
        }
        a.d(context);
    }

    private static void g(Context context) {
        context.deleteFile(k(context));
        context.deleteFile(l(context));
    }

    private static void g(Context context, String str) {
        SharedPreferences a2 = a(context);
        if (str != null && !"".equals(str)) {
            a2.edit().putString(UmengConstants.AtomKey_User_ID, str).commit();
        } else if (UmengConstants.testMode) {
            Log.i(UmengConstants.LOG_TAG, "userID is null or empty");
        }
    }

    public static String getConfigParams(Context context, String str) {
        return m(context).getString(str, "");
    }

    public static JSONObject getUpdateInfo() {
        return k;
    }

    private static SharedPreferences h(Context context) {
        return context.getSharedPreferences("mobclick_agent_header_" + context.getPackageName(), 0);
    }

    private static SharedPreferences i(Context context) {
        return context.getSharedPreferences("mobclick_agent_update_" + context.getPackageName(), 0);
    }

    public static boolean isDownloadingAPK() {
        return a.b();
    }

    private static SharedPreferences j(Context context) {
        return context.getSharedPreferences("mobclick_agent_state_" + context.getPackageName(), 0);
    }

    private static String k(Context context) {
        return "mobclick_agent_header_" + context.getPackageName();
    }

    private static String l(Context context) {
        return "mobclick_agent_cached_" + context.getPackageName();
    }

    private static SharedPreferences m(Context context) {
        return context.getSharedPreferences("mobclick_agent_online_setting_" + context.getPackageName(), 0);
    }

    private static int n(Context context) {
        SharedPreferences m = m(context);
        return (!m.contains(UmengConstants.Online_Config_Net_Policy) || m.getInt(UmengConstants.Online_Config_Net_Policy, -1) == -1) ? m.getInt(UmengConstants.Online_Config_Local_Policy, b) : m.getInt(UmengConstants.Online_Config_Net_Policy, b);
    }

    private static String o(Context context) {
        return m(context).getString(UmengConstants.Online_Config_Last_Modify, "");
    }

    public static void onError(Context context) {
        try {
            String b2 = l.b(context);
            if (b2 == null || b2.length() == 0) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey in onError");
                }
            } else if (context != null) {
                new j(context, b2, 2).start();
            } else if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context in onError");
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onError()");
                e2.printStackTrace();
            }
        }
    }

    public static void onEvent(Context context, String str) {
        onEvent(context, str, 1);
    }

    public static void onEvent(Context context, String str, int i2) {
        onEvent(context, str, (String) null, i2);
    }

    public static void onEvent(Context context, String str, String str2) {
        if (str2 != null && str2 != "") {
            onEvent(context, str, str2, 1);
        } else if (UmengConstants.testMode) {
            Log.e(UmengConstants.LOG_TAG, "label is null or empty in onEvent(4p)");
        }
    }

    public static void onEvent(Context context, String str, String str2, int i2) {
        try {
            String b2 = l.b(context);
            if (b2 == null || b2.length() == 0) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey in onEvent(4p)");
                }
            } else if (context == null) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected null context in onEvent(4p)");
                }
            } else if (str == null || str == "") {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "tag is null or empty in onEvent(4p)");
                }
            } else if (i2 > 0) {
                new j(context, b2, str, str2, i2, 3).start();
            } else if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Illegal value of acc in onEvent(4p)");
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onEvent(). ");
                e2.printStackTrace();
            }
        }
    }

    public static void onPause(Context context) {
        if (context == null) {
            try {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected null context in onPause");
                }
            } catch (Exception e2) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onRause(). ");
                    e2.printStackTrace();
                }
            }
        } else {
            new j(context, 0).start();
        }
    }

    public static void onResume(Context context) {
        onResume(context, l.b(context), l.g(context));
    }

    public static void onResume(Context context, String str, String str2) {
        try {
            UmengConstants.channel = str2;
            if (context == null) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "unexpected null context in onResume");
                }
            } else if (str != null && str.length() != 0) {
                new j(context, str, str2, 1).start();
            } else if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey in onResume");
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onResume(). ");
                e2.printStackTrace();
            }
        }
    }

    public static void openActivityDurationTrack(boolean z) {
        UmengConstants.ACTIVITY_DURATION_OPEN = z;
    }

    public static void reportError(Context context, String str) {
        if (str != "" && str.length() <= 10240) {
            a.c(context, str);
        }
    }

    public static void setAutoLocation(boolean z) {
        mUseLocationService = z;
    }

    public static void setDebugMode(boolean z) {
        UmengConstants.testMode = z;
    }

    public static void setDefaultReportPolicy(Context context, int i2) {
        if (i2 >= 0 && i2 <= 5) {
            b = i2;
            a(context, i2);
        } else if (UmengConstants.testMode) {
            Log.e(UmengConstants.LOG_TAG, "Illegal value of report policy");
        }
    }

    public static void setOnlineConfigureListener(UmengOnlineConfigureListener umengOnlineConfigureListener) {
        j = umengOnlineConfigureListener;
    }

    public static void setOpenGLContext(GL10 gl10) {
        if (gl10 != null) {
            String[] a2 = l.a(gl10);
            if (a2.length == 2) {
                GPU_VENDER = a2[0];
                GPU_RENDERER = a2[1];
            }
        }
    }

    public static void setUpdateListener(UmengUpdateListener umengUpdateListener) {
        i = umengUpdateListener;
    }

    public static void setUpdateOnlyWifi(boolean z) {
        mUpdateOnlyWifi = z;
    }

    public static void showUpdateDialog(Context context) {
        if (k != null && k.has("update") && k.optString("update").toLowerCase().equals("yes")) {
            File e2 = e(context, k);
            if (e2 == null || !UmengConstants.enableCacheInUpdate) {
                b(context, k).show();
            } else {
                b(context, e2).show();
            }
        }
    }

    public static void update(Context context) {
        try {
            if (a.b()) {
                l.a(context);
            } else if (!mUpdateOnlyWifi || l.f(context)[0].equals("Wi-Fi")) {
                if (context == null) {
                    if (i != null) {
                        i.onUpdateReturned(UpdateStatus.No);
                    }
                    if (UmengConstants.testMode) {
                        Log.e(UmengConstants.LOG_TAG, "unexpected null context in update");
                        return;
                    }
                    return;
                }
                a.a(context, l.b(context));
            } else if (i != null) {
                i.onUpdateReturned(UpdateStatus.NoneWifi);
            }
        } catch (Exception e2) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.update(). " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    public static void update(Context context, String str) {
        UmengConstants.channel = str;
        update(context);
    }

    public static void updateOnlineConfig(Context context) {
        if (context == null) {
            try {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context in updateOnlineConfig");
            } catch (Exception e2) {
                if (UmengConstants.testMode) {
                    Log.e(UmengConstants.LOG_TAG, "exception in updateOnlineConfig");
                }
            }
        } else {
            a.f(context, l.b(context));
        }
    }

    public static void updateOnlineConfig(Context context, String str) {
        UmengConstants.channel = str;
        updateOnlineConfig(context);
    }
}
