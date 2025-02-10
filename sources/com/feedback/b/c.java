package com.feedback.b;

import android.content.Context;
import android.content.SharedPreferences;
import com.feedback.a.d;
import com.mobclick.android.UmengConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    public static synchronized String a(Context context, JSONArray jSONArray) {
        String str;
        String str2;
        Exception e;
        synchronized (c.class) {
            if (jSONArray.length() == 0) {
                str = "";
            } else {
                String str3 = "";
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        JSONArray jSONArray2 = jSONArray.getJSONArray(i);
                        String str4 = str3;
                        int i2 = 0;
                        while (i2 < jSONArray2.length()) {
                            try {
                                if (!jSONArray2.getString(i2).equals("end")) {
                                    JSONObject jSONObject = jSONArray2.getJSONObject(i2);
                                    if (UmengConstants.Atom_Type_DevReply.equalsIgnoreCase(jSONObject.optString(UmengConstants.AtomKey_Type)) && b(context, jSONObject)) {
                                        str4 = d(context, b.a(jSONObject, UmengConstants.AtomKey_FeedbackID));
                                    }
                                }
                                i2++;
                            } catch (Exception e2) {
                                e = e2;
                                str2 = str4;
                                e.printStackTrace();
                                str3 = str2;
                            }
                        }
                        str3 = str4;
                    } catch (Exception e3) {
                        Exception exc = e3;
                        str2 = str3;
                        e = exc;
                        e.printStackTrace();
                        str3 = str2;
                    }
                }
                str = str3;
            }
        }
        return str;
    }

    public static synchronized List a(Context context) {
        ArrayList arrayList;
        synchronized (c.class) {
            arrayList = new ArrayList();
            try {
                Iterator<?> it = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0).getAll().values().iterator();
                while (it.hasNext()) {
                    arrayList.add(new d(new JSONArray((String) it.next())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static synchronized void a(Context context, d dVar, int i) {
        synchronized (c.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String str = dVar.c;
            String string = sharedPreferences.getString(str, (String) null);
            try {
                JSONArray jSONArray = new JSONArray();
                JSONArray jSONArray2 = new JSONArray(string);
                if (jSONArray2.length() == 1) {
                    edit.remove(dVar.c);
                } else {
                    for (int i2 = 0; i2 <= jSONArray2.length() - 1; i2++) {
                        if (i2 != i) {
                            jSONArray.put(jSONArray2.getJSONObject(i2));
                        }
                    }
                    edit.putString(str, jSONArray.toString());
                }
                edit.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dVar.b(i);
        }
        return;
    }

    public static synchronized void a(Context context, String str) {
        synchronized (c.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            if (!d.a(str)) {
                String str2 = "";
                for (String str3 : sharedPreferences.getString(UmengConstants.TrivialPreKey_newreplyIds, "").split(",")) {
                    if (!d.a(str3) && !str3.equals(str)) {
                        str2 = String.valueOf(str2) + "," + str3.trim();
                    }
                }
                a(sharedPreferences, UmengConstants.TrivialPreKey_newreplyIds, str2);
            }
        }
    }

    public static synchronized void a(Context context, String str, String str2) {
        synchronized (c.class) {
            context.getSharedPreferences(str, 0).edit().remove(str2).commit();
        }
    }

    private static synchronized void a(SharedPreferences sharedPreferences, String str, String str2) {
        synchronized (c.class) {
            sharedPreferences.edit().putString(str, str2).commit();
        }
    }

    public static boolean a(Context context, d dVar) {
        return context.getSharedPreferences(UmengConstants.PreName_Trivial, 0).getString(UmengConstants.TrivialPreKey_newreplyIds, "").contains(dVar.c);
    }

    public static synchronized boolean a(Context context, JSONObject jSONObject) {
        boolean z;
        synchronized (c.class) {
            String a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
            if (!d.a(a)) {
                a(sharedPreferences, a, "[" + jSONObject.toString() + "]");
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public static synchronized d b(Context context, String str) {
        d dVar;
        synchronized (c.class) {
            try {
                dVar = new d(new JSONArray(context.getSharedPreferences(UmengConstants.FeedbackPreName, 0).getString(str, (String) null)));
            } catch (Exception e) {
                e.printStackTrace();
                dVar = null;
            }
        }
        return dVar;
    }

    public static synchronized boolean b(Context context, JSONObject jSONObject) {
        boolean z;
        synchronized (c.class) {
            String a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
            try {
                JSONArray jSONArray = new JSONArray(sharedPreferences.getString(a, (String) null));
                if (UmengConstants.Atom_Type_UserReply.equals(b.a(jSONObject, UmengConstants.AtomKey_Type))) {
                    d dVar = new d(jSONArray);
                    int length = jSONArray.length() - 1;
                    while (true) {
                        if (length >= 0) {
                            String optString = jSONArray.getJSONObject(length).optString("reply_id", (String) null);
                            String optString2 = jSONObject.optString("reply_id", (String) null);
                            if (optString != null && optString.equalsIgnoreCase(optString2)) {
                                a(context, dVar, length);
                                break;
                            }
                            length--;
                        } else {
                            break;
                        }
                    }
                    JSONArray jSONArray2 = new JSONArray(sharedPreferences.getString(a, (String) null));
                    jSONArray2.put(jSONObject);
                    a(sharedPreferences, a, jSONArray2.toString());
                    z = true;
                } else {
                    SharedPreferences sharedPreferences2 = context.getSharedPreferences(UmengConstants.PreName_maxReplyIdOfFb, 0);
                    String string = sharedPreferences2.getString(a, "RP0");
                    String a2 = b.a(jSONObject, "reply_id");
                    if (!d.a(string, a2)) {
                        jSONArray.put(jSONObject);
                        a(sharedPreferences, a, jSONArray.toString());
                        a(sharedPreferences2, a, a2);
                        a(context.getSharedPreferences(UmengConstants.PreName_Trivial, 0), UmengConstants.TrivialPreKey_MaxReplyID, a2);
                        z = true;
                    }
                    z = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return z;
    }

    public static synchronized void c(Context context, String str) {
        synchronized (c.class) {
            context.getSharedPreferences(UmengConstants.FeedbackPreName, 0).edit().remove(str).commit();
        }
    }

    public static synchronized void c(Context context, JSONObject jSONObject) {
        synchronized (c.class) {
            b.e(jSONObject);
            e(context, jSONObject);
        }
    }

    private static synchronized String d(Context context, String str) {
        String str2;
        synchronized (c.class) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.PreName_Trivial, 0);
            String string = sharedPreferences.getString(UmengConstants.TrivialPreKey_newreplyIds, "");
            if (string.contains(str)) {
                str2 = string;
            } else {
                String str3 = String.valueOf(string) + "," + str;
                a(sharedPreferences, UmengConstants.TrivialPreKey_newreplyIds, str3);
                str2 = str3;
            }
        }
        return str2;
    }

    public static synchronized void d(Context context, JSONObject jSONObject) {
        synchronized (c.class) {
            b.d(jSONObject);
            e(context, jSONObject);
        }
    }

    private static synchronized boolean e(Context context, JSONObject jSONObject) {
        boolean z;
        synchronized (c.class) {
            String a = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
            SharedPreferences sharedPreferences = context.getSharedPreferences(UmengConstants.FeedbackPreName, 0);
            try {
                JSONArray jSONArray = new JSONArray(sharedPreferences.getString(a, "[]"));
                jSONArray.put(jSONObject);
                a(sharedPreferences, a, jSONArray.toString());
                z = true;
            } catch (JSONException e) {
                e.printStackTrace();
                z = false;
            }
        }
        return z;
    }
}
