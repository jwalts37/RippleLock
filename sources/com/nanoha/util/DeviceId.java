package com.nanoha.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nanoha.MyLockScreen_all.R;

public class DeviceId {
    private static final String CHARGE_FILE = (Environment.getExternalStorageDirectory() + "/ripplelock.key");
    String deviceId = this.tm.getDeviceId();
    Context mContext;
    Button registButton;
    String secureDeviceId;
    TelephonyManager tm;
    TextView tv;
    Util util;

    public DeviceId(Context mContext2) {
        this.mContext = mContext2;
        this.util = Util.getInstance(mContext2);
        this.tm = (TelephonyManager) mContext2.getSystemService("phone");
        this.secureDeviceId = Settings.Secure.getString(mContext2.getContentResolver(), "android_id");
    }

    public void regist() {
        if (preRegist()) {
            generateEncrptKeyFile(this.secureDeviceId);
        }
    }

    public boolean checkHasAd() {
        if (!checkHasAd(this.secureDeviceId) || !checkHasAd(this.deviceId)) {
            return false;
        }
        return true;
    }

    public boolean utilRegist() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(this.mContext, R.string.insert_sd, 0).show();
            return false;
        } else if (canRegist(this.secureDeviceId)) {
            generateEncrptKeyFile(this.secureDeviceId);
            return true;
        } else if (!canRegist(this.deviceId)) {
            return true;
        } else {
            generateEncrptKeyFile(this.deviceId);
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0099 A[SYNTHETIC, Splitter:B:29:0x0099] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009e A[Catch:{ Exception -> 0x00a2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ac A[SYNTHETIC, Splitter:B:37:0x00ac] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b1 A[Catch:{ Exception -> 0x00b5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkHasAd(java.lang.String r21) {
        /*
            r20 = this;
            r14 = 1
            java.lang.String r16 = android.os.Environment.getExternalStorageState()
            java.lang.String r17 = "mounted"
            boolean r16 = r16.equals(r17)
            if (r16 == 0) goto L_0x0055
            java.io.File r5 = new java.io.File
            java.lang.String r16 = CHARGE_FILE
            r0 = r5
            r1 = r16
            r0.<init>(r1)
            boolean r16 = r5.exists()
            if (r16 == 0) goto L_0x0055
            r8 = 0
            r2 = 0
            java.io.FileReader r9 = new java.io.FileReader     // Catch:{ Exception -> 0x0091 }
            r9.<init>(r5)     // Catch:{ Exception -> 0x0091 }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00ca, all -> 0x00c3 }
            r3.<init>(r9)     // Catch:{ Exception -> 0x00ca, all -> 0x00c3 }
            java.lang.String r12 = r3.readLine()     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            java.lang.String r16 = ","
            r0 = r12
            r1 = r16
            java.lang.String[] r13 = r0.split(r1)     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            int r11 = r13.length     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            byte[] r6 = new byte[r11]     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r10 = 0
        L_0x003a:
            if (r10 < r11) goto L_0x0056
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r4.<init>(r6)     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r0 = r4
            r1 = r21
            boolean r16 = r0.equals(r1)     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            if (r16 == 0) goto L_0x004b
            r14 = 0
        L_0x004b:
            if (r9 == 0) goto L_0x0050
            r9.close()     // Catch:{ Exception -> 0x00bc }
        L_0x0050:
            if (r3 == 0) goto L_0x0055
            r3.close()     // Catch:{ Exception -> 0x00bc }
        L_0x0055:
            return r14
        L_0x0056:
            r16 = r13[r10]     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            float r15 = java.lang.Float.parseFloat(r16)     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r16 = 1116209152(0x42880000, float:68.0)
            float r16 = r16 + r15
            int r17 = r10 + 78
            int r17 = r17 * 34
            r0 = r17
            float r0 = (float) r0     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r17 = r0
            r18 = 1078523331(0x4048f5c3, float:3.14)
            r0 = r10
            float r0 = (float) r0     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r19 = r0
            float r18 = r18 * r19
            float r17 = r17 + r18
            float r16 = r16 - r17
            r17 = 1077936128(0x40400000, float:3.0)
            float r16 = r16 / r17
            r17 = 1107820544(0x42080000, float:34.0)
            float r16 = r16 - r17
            r17 = 1084227584(0x40a00000, float:5.0)
            float r16 = r16 / r17
            r0 = r16
            int r0 = (int) r0     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r16 = r0
            r0 = r16
            byte r0 = (byte) r0     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            r16 = r0
            r6[r10] = r16     // Catch:{ Exception -> 0x00cf, all -> 0x00c6 }
            int r10 = r10 + 1
            goto L_0x003a
        L_0x0091:
            r16 = move-exception
            r7 = r16
        L_0x0094:
            r7.printStackTrace()     // Catch:{ all -> 0x00a9 }
            if (r8 == 0) goto L_0x009c
            r8.close()     // Catch:{ Exception -> 0x00a2 }
        L_0x009c:
            if (r2 == 0) goto L_0x0055
            r2.close()     // Catch:{ Exception -> 0x00a2 }
            goto L_0x0055
        L_0x00a2:
            r16 = move-exception
            r7 = r16
            r7.printStackTrace()
            goto L_0x0055
        L_0x00a9:
            r16 = move-exception
        L_0x00aa:
            if (r8 == 0) goto L_0x00af
            r8.close()     // Catch:{ Exception -> 0x00b5 }
        L_0x00af:
            if (r2 == 0) goto L_0x00b4
            r2.close()     // Catch:{ Exception -> 0x00b5 }
        L_0x00b4:
            throw r16
        L_0x00b5:
            r17 = move-exception
            r7 = r17
            r7.printStackTrace()
            goto L_0x00b4
        L_0x00bc:
            r16 = move-exception
            r7 = r16
            r7.printStackTrace()
            goto L_0x0055
        L_0x00c3:
            r16 = move-exception
            r8 = r9
            goto L_0x00aa
        L_0x00c6:
            r16 = move-exception
            r2 = r3
            r8 = r9
            goto L_0x00aa
        L_0x00ca:
            r16 = move-exception
            r7 = r16
            r8 = r9
            goto L_0x0094
        L_0x00cf:
            r16 = move-exception
            r7 = r16
            r2 = r3
            r8 = r9
            goto L_0x0094
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nanoha.util.DeviceId.checkHasAd(java.lang.String):boolean");
    }

    public void callUtilRegist() {
        this.util.registFreeVersion();
    }

    public boolean preRegist() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(this.mContext, R.string.insert_sd, 0).show();
            return false;
        } else if (canRegist(this.secureDeviceId) || canRegist(this.deviceId)) {
            return true;
        } else {
            Toast.makeText(this.mContext, R.string.can_not_regist, 0).show();
            return false;
        }
    }

    public boolean canRegist(String deviceId2) {
        String[] readStrArray = encrptDeviceId(deviceId2).split(",");
        int length = readStrArray.length;
        byte[] deviceIdBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            deviceIdBytes[i] = (byte) ((int) (((((68.0f + Float.parseFloat(readStrArray[i])) - (((float) ((i + 78) * 34)) + (3.14f * ((float) i)))) / 3.0f) - 34.0f) / 5.0f));
        }
        if (new String(deviceIdBytes).equals(deviceId2)) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x002e A[SYNTHETIC, Splitter:B:18:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033 A[Catch:{ Exception -> 0x0037 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0040 A[SYNTHETIC, Splitter:B:26:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0045 A[Catch:{ Exception -> 0x0049 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void generateEncrptKeyFile(java.lang.String r9) {
        /*
            r8 = this;
            java.io.File r2 = new java.io.File
            java.lang.String r6 = CHARGE_FILE
            r2.<init>(r6)
            r4 = 0
            r0 = 0
            java.io.FileWriter r5 = new java.io.FileWriter     // Catch:{ Exception -> 0x0027 }
            r5.<init>(r2)     // Catch:{ Exception -> 0x0027 }
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x005e, all -> 0x0057 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x005e, all -> 0x0057 }
            java.lang.String r6 = r8.encrptDeviceId(r9)     // Catch:{ Exception -> 0x0062, all -> 0x005a }
            r1.write(r6)     // Catch:{ Exception -> 0x0062, all -> 0x005a }
            if (r1 == 0) goto L_0x001f
            r1.close()     // Catch:{ Exception -> 0x004f }
        L_0x001f:
            if (r5 == 0) goto L_0x0054
            r5.close()     // Catch:{ Exception -> 0x004f }
            r0 = r1
            r4 = r5
        L_0x0026:
            return
        L_0x0027:
            r6 = move-exception
            r3 = r6
        L_0x0029:
            r3.printStackTrace()     // Catch:{ all -> 0x003d }
            if (r0 == 0) goto L_0x0031
            r0.close()     // Catch:{ Exception -> 0x0037 }
        L_0x0031:
            if (r4 == 0) goto L_0x0026
            r4.close()     // Catch:{ Exception -> 0x0037 }
            goto L_0x0026
        L_0x0037:
            r6 = move-exception
            r3 = r6
            r3.printStackTrace()
            goto L_0x0026
        L_0x003d:
            r6 = move-exception
        L_0x003e:
            if (r0 == 0) goto L_0x0043
            r0.close()     // Catch:{ Exception -> 0x0049 }
        L_0x0043:
            if (r4 == 0) goto L_0x0048
            r4.close()     // Catch:{ Exception -> 0x0049 }
        L_0x0048:
            throw r6
        L_0x0049:
            r7 = move-exception
            r3 = r7
            r3.printStackTrace()
            goto L_0x0048
        L_0x004f:
            r6 = move-exception
            r3 = r6
            r3.printStackTrace()
        L_0x0054:
            r0 = r1
            r4 = r5
            goto L_0x0026
        L_0x0057:
            r6 = move-exception
            r4 = r5
            goto L_0x003e
        L_0x005a:
            r6 = move-exception
            r0 = r1
            r4 = r5
            goto L_0x003e
        L_0x005e:
            r6 = move-exception
            r3 = r6
            r4 = r5
            goto L_0x0029
        L_0x0062:
            r6 = move-exception
            r3 = r6
            r0 = r1
            r4 = r5
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nanoha.util.DeviceId.generateEncrptKeyFile(java.lang.String):void");
    }

    public String encrptDeviceId(String deviceId2) {
        String ret = deviceId2;
        try {
            byte[] deviceBytes = deviceId2.getBytes();
            float[] encrptFloat = new float[deviceBytes.length];
            int length = deviceBytes.length;
            for (int i = 0; i < length; i++) {
                encrptFloat[i] = ((float) ((((deviceBytes[i] * 5) + 34) * 3) - 68)) + ((float) ((i + 78) * 34)) + (3.14f * ((float) i));
            }
            StringBuffer encrptValue = new StringBuffer();
            for (int j = 0; j < length; j++) {
                encrptValue.append(encrptFloat[j]);
                encrptValue.append(",");
            }
            return String.valueOf(encrptValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
    }
}
