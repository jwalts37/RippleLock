package com.feedback.ui;

import android.view.View;
import com.feedback.b.a;

class e implements View.OnClickListener {
    final /* synthetic */ FeedbackConversations a;

    e(FeedbackConversations feedbackConversations) {
        this.a = feedbackConversations;
    }

    public void onClick(View view) {
        a.a(this.a);
    }
}
