package com.feedback.b;

import android.content.Context;
import android.util.Log;
import com.mobclick.android.UmengConstants;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class d {
    static final String a = "Util";
    private static final int b = 30000;

    public static String a() {
        return "RP" + String.valueOf(System.currentTimeMillis()) + String.valueOf((int) (1000.0d + (Math.random() * 9000.0d)));
    }

    public static String a(String str, String str2, String str3) {
        return String.valueOf(str) + "[" + str2 + "_" + str3 + "]" + String.valueOf(System.currentTimeMillis()) + String.valueOf((int) (1000.0d + (Math.random() * 9000.0d)));
    }

    public static String a(Date date, Context context) {
        if (date == null) {
            return "";
        }
        Locale locale = context.getResources().getConfiguration().locale;
        return (Locale.CHINA.equals(locale) ? new SimpleDateFormat("M月d日", locale) : new SimpleDateFormat("dd MMMM", locale)).format(date);
    }

    public static String a(JSONObject jSONObject, String str, String str2) {
        Log.d(a, "JSONObject to string :" + jSONObject.toString() + " END");
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new BasicNameValuePair(str2, jSONObject.toString()));
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(arrayList, "UTF-8");
            HttpPost httpPost = new HttpPost(str);
            httpPost.addHeader(urlEncodedFormEntity.getContentType());
            httpPost.setEntity(urlEncodedFormEntity);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpParams params = defaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, b);
            HttpConnectionParams.setSoTimeout(params, b);
            ConnManagerParams.setTimeout(params, UmengConstants.kContinueSessionMillis);
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(execute.getEntity());
            }
            Log.d(a, "Failed to send message.");
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean a(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean a(String str, String str2) {
        Log.i(a, "[reply_id_of_b]" + str2);
        if (str == null || str2 == null) {
            return false;
        }
        try {
            return Double.parseDouble(str.substring(2)) >= Double.parseDouble(str2.substring(2));
        } catch (Exception e) {
            Log.w(a, "reply id invalid.LocalMaxReplyId:" + str + "reply_id_of_b:" + str2);
            return true;
        }
    }

    public static String b(Date date, Context context) {
        return date == null ? "" : new SimpleDateFormat("yyyy-M-d HH:mm", context.getResources().getConfiguration().locale).format(date);
    }
}
