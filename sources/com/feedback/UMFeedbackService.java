package com.feedback;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.feedback.b.a;
import com.feedback.c.b;
import com.feedback.ui.FeedbackConversations;
import com.mobclick.android.l;

public class UMFeedbackService {
    private static NotificationType a;
    /* access modifiers changed from: private */
    public static Context b;
    private static boolean c = false;

    /* access modifiers changed from: private */
    public static void b(String str) {
        CharSequence charSequence;
        if (a == NotificationType.NotificationBar) {
            NotificationManager notificationManager = (NotificationManager) b.getSystemService("notification");
            Notification notification = new Notification(17301598, b.getString(l.a(b, "string", "UMNewReplyFlick")), System.currentTimeMillis());
            Intent intent = new Intent(b, FeedbackConversations.class);
            intent.setFlags(131072);
            PendingIntent activity = PendingIntent.getActivity(b, 0, intent, 0);
            PackageManager packageManager = b.getPackageManager();
            try {
                charSequence = packageManager.getApplicationLabel(packageManager.getApplicationInfo(b.getPackageName(), 128));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                charSequence = null;
            }
            if (charSequence != null) {
                charSequence = String.valueOf(charSequence) + " : ";
            }
            notification.setLatestEventInfo(b, charSequence + b.getString(l.a(b, "string", "UMNewReplyTitle")), b.getString(l.a(b, "string", "UMNewReplyHint")), activity);
            notification.flags = 16;
            notificationManager.notify(0, notification);
            return;
        }
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(b).inflate(l.a(b, "layout", "umeng_analyse_new_reply_alert_dialog"), (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_new_dev_reply_box"));
        textView.setText(str);
        AlertDialog create = new AlertDialog.Builder(b).create();
        create.show();
        create.setContentView(linearLayout);
        ((TextView) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_new_reply_alert_title"))).setText(b.getString(l.a(b, "string", "UMNewReplyAlertTitle")));
        ((Button) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_exitBtn"))).setOnClickListener(new a(create));
        b bVar = new b(create);
        ((Button) linearLayout.findViewById(l.a(b, "id", "umeng_analyse_see_detail_btn"))).setOnClickListener(bVar);
        textView.setOnClickListener(bVar);
    }

    public static void enableNewReplyNotification(Context context, NotificationType notificationType) {
        c cVar = new c();
        b = context;
        a = notificationType;
        new b(context, (Handler) cVar).start();
        c = true;
    }

    public static boolean getHasCheckedReply() {
        return c;
    }

    public static void openUmengFeedbackSDK(Context context) {
        a.a(context);
    }
}
