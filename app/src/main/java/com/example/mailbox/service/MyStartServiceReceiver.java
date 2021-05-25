package com.example.mailbox.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mailbox.util.NetworkUtil;
import com.example.mailbox.util.Util;

import static android.content.Context.ALARM_SERVICE;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Util.setAlarm(context);
        }
    }

}
