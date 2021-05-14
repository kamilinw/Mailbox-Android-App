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
import android.widget.Toast;

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
    private static final String TAG = "Alarm Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm recieved");

        UserUtil.downloadUserData(context,false, null, true);

    }


}
