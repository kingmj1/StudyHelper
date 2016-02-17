package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by reynolpt on 2/16/2016.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Launch the notification
        Notification notification = intent.getParcelableExtra(TaskDetailActivity.KEY_NOTIFICATION);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(TaskDetailActivity.NOTIFICATION_ID, notification);
    }
}
