package com.example.mailbox.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mailbox.R;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.ui.main.MainActivity;
import com.example.mailbox.util.UserUtil;
import com.google.common.primitives.Longs;

import java.util.ArrayList;
import java.util.List;

public class CommunicationService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "10002";
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            Log.i("Service", "In handler doing staff");

            UserUtil.downloadUserData(getApplicationContext(),false, null);

            UserDatabase userDatabase = UserDatabase.getInstance(getApplicationContext());
            List<Long> mailboxIds = userDatabase.getMailboxIds();

            for (Long id : mailboxIds) {
                if (id < 0)
                    break;
                MailboxDatabase mailboxDatabase = MailboxDatabase.getInstance(getApplicationContext());
                boolean newMail = (Integer) mailboxDatabase.getMailboxField(MailboxDatabase.COLUMN_NAME_NEW_MAIL, id)!=0;

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                boolean isNotificationShowed = sharedPref.getBoolean("isNotificationShowed", false);

                if (newMail && !isNotificationShowed){
                    Log.i("Service", "Showing notification");
                    createNotification(getApplicationContext());
                    editor.putBoolean("isNotificationShowed", true);
                } else {
                    editor.putBoolean("isNotificationShowed", false);
                }
                editor.apply();
            }

            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onDestroy() {
        Log.i("Service", "Stop service");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Thread.NORM_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "Start service");
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void createNotification(Context context)
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
            channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200});
            channel.setBypassDnd(false);
            channel.setSound(null, null);
            manager.createNotificationChannel(channel);
        }


        String notificationTitle = "Nowa wiadomość";
        String notificationContent = "masz nową wiadomosc";

        manager.notify(0, new NotificationCompat.Builder(context,
                NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_mail)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build());


    }
}

