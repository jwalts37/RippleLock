package com.feedback.c;

import android.content.Context;
import android.content.Intent;
import com.feedback.b.b;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import org.json.JSONObject;

public class a extends Thread {
    static String a = "PostFeedbackThread";
    JSONObject b;
    Context c;
    String d;
    String e;
    int f;
    int g;

    public a(JSONObject jSONObject, Context context) {
        b.c(jSONObject);
        this.b = jSONObject;
        this.c = context;
    }

    public void run() {
        JSONObject jSONObject = null;
        Intent action = new Intent().setAction(UmengConstants.PostFeedbackBroadcastAction);
        try {
            if (UmengConstants.Atom_Type_NewFeedback.equals(this.b.optString(UmengConstants.AtomKey_Type))) {
                String a2 = d.a(this.b, UmengConstants.FEEDBACK_NewFeedback_URL, UmengConstants.FeedbackPreName);
                action.putExtra(UmengConstants.AtomKey_Type, UmengConstants.FeedbackPreName);
                jSONObject = new JSONObject(a2);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String a3 = b.a(this.b, UmengConstants.AtomKey_FeedbackID);
        c.a(this.c, UmengConstants.FeedbackPreName, a3);
        if (b.b(jSONObject)) {
            b.f(this.b);
            c.a(this.c, this.b);
            action.putExtra(UmengConstants.PostFeedbackBroadcast, UmengConstants.PostFeedbackBroadcast_Succeed);
        } else {
            b.d(this.b);
            c.d(this.c, this.b);
            action.putExtra(UmengConstants.PostFeedbackBroadcast, "fail");
        }
        action.putExtra(UmengConstants.AtomKey_FeedbackID, a3);
        this.c.sendBroadcast(action);
    }
}
