package com.mobclick.android;

import android.content.Context;
import org.json.JSONObject;

final class k implements Runnable {
    private static final Object a = new Object();
    private MobclickAgent b = MobclickAgent.a;
    private Context c;
    private JSONObject d;

    k(MobclickAgent mobclickAgent, Context context, JSONObject jSONObject) {
        this.c = context;
        this.d = jSONObject;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r4 = this;
            org.json.JSONObject r0 = r4.d     // Catch:{ Exception -> 0x0034 }
            java.lang.String r1 = "type"
            java.lang.String r0 = r0.getString(r1)     // Catch:{ Exception -> 0x0034 }
            java.lang.String r1 = "update"
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x0034 }
            if (r0 == 0) goto L_0x001a
            com.mobclick.android.MobclickAgent r0 = r4.b     // Catch:{ Exception -> 0x0034 }
            android.content.Context r1 = r4.c     // Catch:{ Exception -> 0x0034 }
            org.json.JSONObject r2 = r4.d     // Catch:{ Exception -> 0x0034 }
            r0.a((android.content.Context) r1, (org.json.JSONObject) r2)     // Catch:{ Exception -> 0x0034 }
        L_0x0019:
            return
        L_0x001a:
            org.json.JSONObject r0 = r4.d     // Catch:{ Exception -> 0x0034 }
            java.lang.String r1 = "type"
            java.lang.String r0 = r0.getString(r1)     // Catch:{ Exception -> 0x0034 }
            java.lang.String r1 = "online_config"
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x0034 }
            if (r0 == 0) goto L_0x0040
            com.mobclick.android.MobclickAgent r0 = r4.b     // Catch:{ Exception -> 0x0034 }
            android.content.Context r1 = r4.c     // Catch:{ Exception -> 0x0034 }
            org.json.JSONObject r2 = r4.d     // Catch:{ Exception -> 0x0034 }
            r0.f((android.content.Context) r1, (org.json.JSONObject) r2)     // Catch:{ Exception -> 0x0034 }
            goto L_0x0019
        L_0x0034:
            r0 = move-exception
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r2 = "Exception occurred in ReportMessageHandler"
            android.util.Log.e(r1, r2)
            r0.printStackTrace()
            goto L_0x0019
        L_0x0040:
            java.lang.Object r0 = a     // Catch:{ Exception -> 0x0034 }
            monitor-enter(r0)     // Catch:{ Exception -> 0x0034 }
            com.mobclick.android.MobclickAgent r1 = r4.b     // Catch:{ all -> 0x004e }
            android.content.Context r2 = r4.c     // Catch:{ all -> 0x004e }
            org.json.JSONObject r3 = r4.d     // Catch:{ all -> 0x004e }
            r1.c((android.content.Context) r2, (org.json.JSONObject) r3)     // Catch:{ all -> 0x004e }
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            goto L_0x0019
        L_0x004e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            throw r1     // Catch:{ Exception -> 0x0034 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.k.run():void");
    }
}
