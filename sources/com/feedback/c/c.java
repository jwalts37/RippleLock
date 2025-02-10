package com.feedback.c;

import android.content.Context;
import java.util.concurrent.Callable;
import org.json.JSONObject;

public class c implements Callable {
    static String a = "MsgWorker";
    JSONObject b;
    Context c;

    public c(JSONObject jSONObject, Context context) {
        this.b = jSONObject;
        this.c = context;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x003a  */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Boolean call() {
        /*
            r5 = this;
            r4 = 0
            org.json.JSONObject r0 = r5.b
            java.lang.String r1 = "feedback_id"
            java.lang.String r0 = com.feedback.b.b.a(r0, r1)
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            java.lang.String r2 = "postFeedbackFinished"
            android.content.Intent r1 = r1.setAction(r2)
            java.lang.String r2 = "type"
            java.lang.String r3 = "user_reply"
            android.content.Intent r1 = r1.putExtra(r2, r3)
            java.lang.String r2 = "feedback_id"
            android.content.Intent r0 = r1.putExtra(r2, r0)
            org.json.JSONObject r1 = r5.b     // Catch:{ Exception -> 0x0053 }
            java.lang.String r2 = "http://feedback.whalecloud.com/feedback/reply"
            java.lang.String r3 = "reply"
            java.lang.String r1 = com.feedback.b.d.a((org.json.JSONObject) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0053 }
            if (r1 == 0) goto L_0x0057
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0053 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0053 }
            r1 = r2
        L_0x0034:
            boolean r1 = com.feedback.b.b.b(r1)
            if (r1 == 0) goto L_0x0059
            org.json.JSONObject r1 = r5.b
            com.feedback.b.b.f(r1)
            java.lang.String r1 = "PostFeedbackBroadcast"
            java.lang.String r2 = "succeed"
            r0.putExtra(r1, r2)
        L_0x0046:
            android.content.Context r1 = r5.c
            org.json.JSONObject r2 = r5.b
            com.feedback.b.c.b((android.content.Context) r1, (org.json.JSONObject) r2)
            android.content.Context r1 = r5.c
            r1.sendBroadcast(r0)
            return r4
        L_0x0053:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0057:
            r1 = r4
            goto L_0x0034
        L_0x0059:
            org.json.JSONObject r1 = r5.b
            com.feedback.b.b.d(r1)
            java.lang.String r1 = "PostFeedbackBroadcast"
            java.lang.String r2 = "fail"
            r0.putExtra(r1, r2)
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: com.feedback.c.c.call():java.lang.Boolean");
    }
}
