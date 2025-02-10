package com.feedback.a;

import com.feedback.b.b;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import java.util.Date;
import org.json.JSONObject;

public class a implements Comparable {
    String a;
    String b;
    public String c;
    public String d;
    public Date e;
    public c f;
    public b g;

    public a(JSONObject jSONObject) {
        if (jSONObject == null) {
            throw new Exception("invalid atom");
        }
        String optString = jSONObject.optString(UmengConstants.AtomKey_Type);
        if (UmengConstants.Atom_Type_NewFeedback.equals(optString)) {
            this.f = c.Starting;
        } else if (UmengConstants.Atom_Type_DevReply.equals(optString)) {
            this.f = c.DevReply;
        } else if (UmengConstants.Atom_Type_UserReply.equals(optString)) {
            this.f = c.UserReply;
        }
        String a2 = b.a(jSONObject, UmengConstants.AtomKey_State);
        if (UmengConstants.TempState.equalsIgnoreCase(a2)) {
            this.g = b.Sending;
        } else if ("fail".equalsIgnoreCase(a2)) {
            this.g = b.Fail;
        } else if ("ok".equalsIgnoreCase(a2)) {
            this.g = b.OK;
        } else {
            this.g = b.OK;
        }
        if (this.f == c.Starting) {
            this.a = b.a(jSONObject, UmengConstants.AtomKey_Thread_Title);
        }
        this.b = b.a(jSONObject, UmengConstants.AtomKey_Thread_Title);
        if (d.a(this.b)) {
            this.b = b.a(jSONObject, UmengConstants.AtomKey_Content);
        }
        this.c = b.a(jSONObject, UmengConstants.AtomKey_FeedbackID);
        this.e = l.d(b.a(jSONObject, UmengConstants.AtomKey_Date));
    }

    /* renamed from: a */
    public int compareTo(a aVar) {
        Date date = aVar.e;
        if (this.e == null || date == null || date.equals(this.e)) {
            return 0;
        }
        return date.after(this.e) ? -1 : 1;
    }

    public String a() {
        return this.f == c.Starting ? this.a : this.b;
    }
}
