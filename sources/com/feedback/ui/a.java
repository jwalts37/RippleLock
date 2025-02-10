package com.feedback.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.feedback.b.b;
import com.feedback.b.c;
import com.mobclick.android.l;
import org.json.JSONObject;

class a implements View.OnClickListener {
    final /* synthetic */ FeedbackConversation a;

    a(FeedbackConversation feedbackConversation) {
        this.a = feedbackConversation;
    }

    public void onClick(View view) {
        String editable = this.a.h.getText().toString();
        if (editable != null && editable.trim().length() != 0) {
            if (editable.length() > 140) {
                Toast.makeText(this.a, this.a.getString(l.a(this.a, "string", "UMContentTooLong")), 0).show();
                return;
            }
            JSONObject jSONObject = null;
            try {
                jSONObject = b.a((Context) this.a, editable, this.a.e.c);
            } catch (Exception e) {
                Toast.makeText(this.a, e.getMessage(), 0).show();
                c.d((Context) this.a, (JSONObject) null);
                Log.d(FeedbackConversation.c, e.getMessage());
            }
            this.a.h.setText("");
            ((InputMethodManager) this.a.getSystemService("input_method")).hideSoftInputFromWindow(this.a.h.getWindowToken(), 0);
            c.c((Context) this.a, jSONObject);
            this.a.e = c.b((Context) this.a, this.a.e.c);
            this.a.f.a(this.a.e);
            this.a.f.notifyDataSetChanged();
            this.a.setSelection(this.a.f.getCount() - 1);
            FeedbackConversation.executorService.submit(new com.feedback.c.c(jSONObject, this.a));
        }
    }
}
