package com.feedback.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.feedback.a.e;
import com.feedback.b.c;
import com.mobclick.android.UmengConstants;

class b extends BroadcastReceiver {
    final /* synthetic */ FeedbackConversation a;

    private b(FeedbackConversation feedbackConversation) {
        this.a = feedbackConversation;
    }

    /* synthetic */ b(FeedbackConversation feedbackConversation, b bVar) {
        this(feedbackConversation);
    }

    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(UmengConstants.AtomKey_FeedbackID);
        if (this.a.e.c.equalsIgnoreCase(stringExtra)) {
            this.a.e = c.b((Context) this.a, stringExtra);
            this.a.f.a(this.a.e);
            this.a.f.notifyDataSetChanged();
        }
        if (this.a.e.b != e.Other) {
            this.a.h.setEnabled(false);
            this.a.i.setEnabled(false);
            return;
        }
        this.a.h.setEnabled(true);
        this.a.i.setEnabled(true);
    }
}
