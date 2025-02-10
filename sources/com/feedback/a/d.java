package com.feedback.a;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class d implements Comparable {
    public String a = d.class.getSimpleName();
    public e b = e.Other;
    public String c;
    public a d;
    public a e;
    public List f = new ArrayList();

    public d(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                this.f.add(new a(jSONArray.getJSONObject(i)));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        if (!this.f.isEmpty()) {
            this.d = (a) this.f.get(0);
            this.e = (a) this.f.get(this.f.size() - 1);
            this.c = this.d.c;
            if (this.f.size() != 1) {
                return;
            }
            if (((a) this.f.get(0)).g == b.Fail) {
                this.b = e.PureFail;
            } else if (((a) this.f.get(0)).g == b.Sending) {
                this.b = e.PureSending;
            }
        }
    }

    /* renamed from: a */
    public int compareTo(d dVar) {
        Date date = this.e.e;
        Date date2 = dVar.e.e;
        if (date2 == null || date == null || date.equals(date2)) {
            return 0;
        }
        return date.after(date2) ? -1 : 1;
    }

    public a a(int i) {
        if (i < 0 || i > this.f.size() - 1) {
            return null;
        }
        return (a) this.f.get(i);
    }

    public void b(int i) {
        if (i >= 0 && i <= this.f.size() - 1) {
            this.f.remove(i);
        }
    }
}
