package com.feedback;

import android.app.AlertDialog;
import android.view.View;

class a implements View.OnClickListener {
    private final /* synthetic */ AlertDialog a;

    a(AlertDialog alertDialog) {
        this.a = alertDialog;
    }

    public void onClick(View view) {
        this.a.dismiss();
    }
}
