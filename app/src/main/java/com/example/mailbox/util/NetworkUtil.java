package com.example.mailbox.util;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mailbox.R;
import com.example.mailbox.service.RefreshJobService;

import org.jetbrains.annotations.NotNull;

public class NetworkUtil {

    /**
     * Check if there is an internet connection. If there is not, displays
     * alert dialog.
     * @param context context
     * @return Returns true if there is internet connection. Otherwise false.
     */
    public static boolean isNoInternetConnection(@NotNull Context context, boolean alert){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        boolean connection = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (connection){
            return false;
        } else {
            if (alert)
                infoAlertDialog(context, R.string.no_internet_connection);
            return true;
        }
    }

    /**
     * Builds an info alert dialog
     * @param context context
     * @param title Alert dialog title
     */
    public static void infoAlertDialog(Context context, int title){
        new AlertDialog.Builder(context)
                .setMessage(title)
                .setPositiveButton("OK",null)
                .show();
    }

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, RefreshJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setPeriodic(10*1000);
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}
