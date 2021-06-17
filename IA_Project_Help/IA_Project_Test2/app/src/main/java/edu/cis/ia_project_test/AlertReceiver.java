package edu.cis.ia_project_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver
{
    //Receive the alert from the AlarmManager and send the notification
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        //the text of the notification
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notifications("Test", "Test");
        //build the notification
        notificationHelper.getManager().notify(1, nb.build());

    }
}
