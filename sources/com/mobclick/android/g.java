package com.mobclick.android;

import android.content.DialogInterface;

class g implements DialogInterface.OnClickListener {
    g() {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
