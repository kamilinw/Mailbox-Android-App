package com.example.mailbox.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.ui.main.MainActivity;
import com.example.mailbox.util.NetworkUtil;
import com.example.mailbox.util.UserUtil;
import com.google.common.primitives.Longs;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "10002";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm Receiver", "Alarm recieved");
        if (NetworkUtil.isNoInternetConnection(context, false)){
            return;
        }


        Intent serviceIntent = new Intent(context, CommunicationService.class);
        context.startService(serviceIntent);
    }


}
