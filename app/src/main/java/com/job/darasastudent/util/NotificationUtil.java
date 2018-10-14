package com.job.darasastudent.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.job.darasastudent.R;
import com.job.darasastudent.ui.MainActivity;


/**
 * Created by Job on Monday : 7/16/2018.
 */
public class NotificationUtil {

    public static final int REPLY_INTENT_ID = 0;
    public static final int ARCHIVE_INTENT_ID = 1;
    public static final String LABEL_REPLY = "Reply";
    public static final String LABEL_ARCHIVE = "Archive";
    public static final String KEY_PRESSED_ACTION = "KEY_PRESSED_ACTION";
    public static final String REPLY_ACTION = "com.job.darasastudent.util.ACTION_MESSAGE_REPLY";


    public void showStandardHeadsUpNotification(Context context,String title, String message) {

        PendingIntent archiveIntent = PendingIntent.getActivity(context,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, archiveIntent)
                        .build();
        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder notificationBuider = createNotificationBuider(
                context, title, message);
        notificationBuider.setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        /*notificationBuider.addAction(replyAction);
        notificationBuider.addAction(archiveAction);*/

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, MainActivity.class);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuider.setFullScreenIntent(fullScreenPendingIntent, true);
        showNotification(context, notificationBuider.build(), 0);
    }

    public NotificationCompat.Builder createNotificationBuider(Context context,
                                                               String title, String message) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setAutoCancel(true);
    }

    private void showNotification(Context context, Notification notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification);
    }

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(KEY_PRESSED_ACTION, label);
    }
}
