package com.mobclick.android;

import android.content.Context;

final class j extends Thread {
    private static final Object a = new Object();
    private Context b;
    private int c;
    private String d;
    private String e;
    private String f;
    private String g;
    private int h;

    j(Context context, int i) {
        this.b = context;
        this.c = i;
    }

    j(Context context, String str, int i) {
        this.b = context;
        this.c = i;
        this.d = str;
    }

    j(Context context, String str, String str2, int i) {
        this.b = context;
        this.c = i;
        this.d = str;
        this.e = str2;
    }

    j(Context context, String str, String str2, String str3, int i, int i2) {
        this.b = context;
        this.d = str;
        this.f = str2;
        this.g = str3;
        this.h = i;
        this.c = i2;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x0021=Splitter:B:16:0x0021, B:12:0x0016=Splitter:B:12:0x0016} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            java.lang.Object r6 = a     // Catch:{ Exception -> 0x0026 }
            monitor-enter(r6)     // Catch:{ Exception -> 0x0026 }
            int r0 = r7.c     // Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x0046
            android.content.Context r0 = r7.b     // Catch:{ Exception -> 0x0036 }
            if (r0 != 0) goto L_0x0018
            boolean r0 = com.mobclick.android.UmengConstants.testMode     // Catch:{ Exception -> 0x0036 }
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = "MobclickAgent"
            java.lang.String r1 = "unexpected null context in invokehander flag=0"
            android.util.Log.e(r0, r1)     // Catch:{ Exception -> 0x0036 }
        L_0x0016:
            monitor-exit(r6)     // Catch:{ all -> 0x0023 }
        L_0x0017:
            return
        L_0x0018:
            com.mobclick.android.MobclickAgent r0 = com.mobclick.android.MobclickAgent.a     // Catch:{ Exception -> 0x0036 }
            android.content.Context r1 = r7.b     // Catch:{ Exception -> 0x0036 }
            r0.c(r1)     // Catch:{ Exception -> 0x0036 }
        L_0x0021:
            monitor-exit(r6)     // Catch:{ all -> 0x0023 }
            goto L_0x0017
        L_0x0023:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0023 }
            throw r0     // Catch:{ Exception -> 0x0026 }
        L_0x0026:
            r0 = move-exception
            boolean r1 = com.mobclick.android.UmengConstants.testMode
            if (r1 == 0) goto L_0x0017
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r2 = "Exception occurred in invokehander."
            android.util.Log.e(r1, r2)
            r0.printStackTrace()
            goto L_0x0017
        L_0x0036:
            r0 = move-exception
            boolean r1 = com.mobclick.android.UmengConstants.testMode     // Catch:{ all -> 0x0023 }
            if (r1 == 0) goto L_0x0021
            java.lang.String r1 = "MobclickAgent"
            java.lang.String r2 = "unexpected null context in invokehander flag=0"
            android.util.Log.e(r1, r2)     // Catch:{ all -> 0x0023 }
            r0.printStackTrace()     // Catch:{ all -> 0x0023 }
            goto L_0x0021
        L_0x0046:
            int r0 = r7.c     // Catch:{ all -> 0x0023 }
            r1 = 1
            if (r0 != r1) goto L_0x0059
            com.mobclick.android.MobclickAgent r0 = com.mobclick.android.MobclickAgent.a     // Catch:{ all -> 0x0023 }
            android.content.Context r1 = r7.b     // Catch:{ all -> 0x0023 }
            java.lang.String r2 = r7.d     // Catch:{ all -> 0x0023 }
            java.lang.String r3 = r7.e     // Catch:{ all -> 0x0023 }
            r0.a((android.content.Context) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0023 }
            goto L_0x0021
        L_0x0059:
            int r0 = r7.c     // Catch:{ all -> 0x0023 }
            r1 = 2
            if (r0 != r1) goto L_0x006a
            com.mobclick.android.MobclickAgent r0 = com.mobclick.android.MobclickAgent.a     // Catch:{ all -> 0x0023 }
            android.content.Context r1 = r7.b     // Catch:{ all -> 0x0023 }
            java.lang.String r2 = r7.d     // Catch:{ all -> 0x0023 }
            r0.b((android.content.Context) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0023 }
            goto L_0x0021
        L_0x006a:
            int r0 = r7.c     // Catch:{ all -> 0x0023 }
            r1 = 3
            if (r0 != r1) goto L_0x0021
            com.mobclick.android.MobclickAgent r0 = com.mobclick.android.MobclickAgent.a     // Catch:{ all -> 0x0023 }
            android.content.Context r1 = r7.b     // Catch:{ all -> 0x0023 }
            java.lang.String r2 = r7.d     // Catch:{ all -> 0x0023 }
            java.lang.String r3 = r7.f     // Catch:{ all -> 0x0023 }
            java.lang.String r4 = r7.g     // Catch:{ all -> 0x0023 }
            int r5 = r7.h     // Catch:{ all -> 0x0023 }
            r0.a((android.content.Context) r1, (java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4, (int) r5)     // Catch:{ all -> 0x0023 }
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.j.run():void");
    }
}
