package com.feedback;

import android.app.AlertDialog;
import android.view.View;
import com.feedback.b.a;

class b implements View.OnClickListener {
    private final /* synthetic */ AlertDialog a;

    b(AlertDialog alertDialog) {
        this.a = alertDialog;
    }

    public void onClick(View view) {
        a.b(UMFeedbackService.b);
        this.a.dismiss();
    }
}
