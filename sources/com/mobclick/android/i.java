package com.mobclick.android;

import android.content.DialogInterface;

class i implements DialogInterface.OnClickListener {
    i() {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
