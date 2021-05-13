package com.example.mailbox.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mailbox.util.NetworkUtil;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkUtil.scheduleJob(context);
    }

}
