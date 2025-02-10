package com.feedback.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.feedback.a.a;
import com.feedback.b.c;
import com.feedback.b.d;
import com.mobclick.android.UmengConstants;
import com.mobclick.android.l;
import org.json.JSONArray;
import org.json.JSONObject;

public class SendFeedback extends Activity {
    static boolean a = true;
    /* access modifiers changed from: private */
    public Spinner b;
    /* access modifiers changed from: private */
    public Spinner c;
    /* access modifiers changed from: private */
    public EditText d;
    private TextView e;
    private TextView f;
    private ImageButton g;
    /* access modifiers changed from: private */
    public JSONObject h;

    private void a() {
        this.b = (Spinner) findViewById(l.a(this, "id", "umeng_analyse_feedback_age_spinner"));
        this.c = (Spinner) findViewById(l.a(this, "id", "umeng_analyse_feedback_gender_spinner"));
        this.e = (TextView) findViewById(l.a(this, "id", "umeng_analyse_feedback_submit"));
        this.d = (EditText) findViewById(l.a(this, "id", "umeng_analyse_feedback_content"));
        this.f = (TextView) findViewById(l.a(this, "id", "umeng_analyse_feedback_umeng_title"));
        this.g = (ImageButton) findViewById(l.a(this, "id", "umeng_analyse_feedback_see_list_btn"));
        if (this.b != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, getResources().getStringArray(l.a(this, "array", "UMageList")));
            arrayAdapter.setDropDownViewResource(17367049);
            this.b.setAdapter(arrayAdapter);
        }
        if (this.c != null) {
            ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, 17367048, getResources().getStringArray(l.a(this, "array", "UMgenderList")));
            arrayAdapter2.setDropDownViewResource(17367049);
            this.c.setAdapter(arrayAdapter2);
        }
        if (this.g != null) {
            this.g.setOnClickListener(new i(this));
        }
        b();
        c();
    }

    private void b() {
        if (this.f != null) {
            this.f.setText(getString(l.a(this, "string", "UMFeedbackUmengTitle")));
        }
        if (this.d != null) {
            this.d.setHint(getString(l.a(this, "string", "UMFeedbackContent")));
        }
        if (this.e != null) {
            this.e.setText(getString(l.a(this, "string", "UMFeedbackSummit")));
        }
    }

    private void c() {
        int e2;
        int d2;
        String stringExtra = getIntent().getStringExtra(UmengConstants.AtomKey_FeedbackID);
        if (!(stringExtra == null || this.d == null)) {
            String string = getSharedPreferences(UmengConstants.FeedbackPreName, 0).getString(stringExtra, (String) null);
            if (!d.a(string)) {
                try {
                    this.d.setText(new a(new JSONArray(string).getJSONObject(0)).a());
                    c.a((Context) this, UmengConstants.FeedbackPreName, stringExtra);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        if (!(this.b == null || (d2 = d()) == -1)) {
            this.b.setSelection(d2);
        }
        if (this.c != null && (e2 = e()) != -1) {
            this.c.setSelection(e2);
        }
    }

    private int d() {
        return getSharedPreferences(UmengConstants.PreName_Trivial, 0).getInt(UmengConstants.TrivialPreKey_AgeGroup, -1);
    }

    private int e() {
        return getSharedPreferences(UmengConstants.PreName_Trivial, 0).getInt(UmengConstants.TrivialPreKey_Sex, -1);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(l.a(this, "layout", "umeng_analyse_send_feedback"));
        a();
        if (this.e != null) {
            this.e.setOnClickListener(new j(this, (j) null));
            if (this.d != null) {
                ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(2, 0);
            }
        }
    }
}
