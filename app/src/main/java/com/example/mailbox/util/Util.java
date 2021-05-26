package com.example.mailbox.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

import com.example.mailbox.R;
import com.example.mailbox.alarm.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;

public class Util {

    public static void setAlarm(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60*1000,pendingIntent);
        }

    }

    public static String formatStringDate(String rawDateString) {
        String[] parts = rawDateString.split("T");
        String[] time = parts[1].split(":");
        String date = String.format("%s %s:%s", parts[0], time[0], time[1]);
        return date;
    }

    public static void buttonEffect(View button, Context context){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(context.getResources().getColor(R.color.blue_dark, context.getTheme()), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
