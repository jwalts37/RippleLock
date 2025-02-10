package com.feedback.c;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.feedback.a.a;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

public class b extends Thread {
    static final String a = b.class.getSimpleName();
    Context b;
    String c;
    String d;
    String e;
    int f;
    Handler g;

    public b(Context context) {
        this.c = "http://feedback.whalecloud.com/feedback/reply";
        this.f = 0;
        this.b = context;
        this.d = l.b(context);
        this.e = l.c(context);
    }

    public b(Context context, int i) {
        this.c = "http://feedback.whalecloud.com/feedback/reply";
        this.f = 0;
        this.b = context;
        this.f = i;
        this.d = l.b(context);
        this.e = l.c(context);
    }

    public b(Context context, Handler handler) {
        this(context);
        this.g = handler;
    }

    public void run() {
        String str;
        String str2 = "";
        Iterator<String> it = this.b.getSharedPreferences(UmengConstants.FeedbackPreName, 0).getAll().keySet().iterator();
        while (true) {
            str = str2;
            if (!it.hasNext()) {
                break;
            }
            str2 = it.next();
            if (str.length() != 0) {
                str2 = String.valueOf(str) + "," + str2;
            }
        }
        String string = this.b.getSharedPreferences(UmengConstants.PreName_Trivial, 0).getString(UmengConstants.TrivialPreKey_MaxReplyID, "RP0");
        if (!d.a(str)) {
            this.c = String.valueOf(this.c) + "?appkey=" + this.d + "&feedback_id=" + str;
            if (!string.equals("RP0")) {
                this.c = String.valueOf(this.c) + "&startkey=" + string;
            }
            Log.d(a, this.c);
            HttpResponse httpResponse = null;
            try {
                httpResponse = new DefaultHttpClient().execute(new HttpGet(this.c));
            } catch (Exception e2) {
                Log.d(a, e2.getMessage());
            }
            Intent intent = new Intent();
            intent.setAction(UmengConstants.RetrieveReplyBroadcastAction);
            if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
                intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
            } else {
                HttpEntity entity = httpResponse.getEntity();
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    InputStream content = entity.getContent();
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = content.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        stringBuffer.append(new String(bArr, 0, read, "UTF-8"));
                    }
                    String stringBuffer2 = stringBuffer.toString();
                    Log.d(a, "JSON RECEIVED :" + stringBuffer2);
                    try {
                        String a2 = c.a(this.b, new JSONArray(stringBuffer2));
                        Log.d(a, "newReplyIds :" + a2);
                        if (a2.length() == 0 || a2.split(",").length == 0) {
                            intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, 0);
                        } else {
                            intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, 1);
                            if (this.g != null) {
                                String[] split = a2.split(",");
                                a aVar = c.b(this.b, split[split.length - 1]).e;
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("newReplyContent", aVar.a());
                                message.setData(bundle);
                                this.g.sendMessage(message);
                            }
                        }
                    } catch (JSONException e3) {
                        intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
                        Log.d(a, e3.getMessage());
                    }
                } catch (Exception e4) {
                    intent.putExtra(UmengConstants.RetrieveReplyBroadcastAction, -1);
                    Log.d(a, e4.getMessage());
                }
            }
            this.b.sendBroadcast(intent);
        }
    }
}
