package com.example.mailbox.util;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mailbox.R;

public class NetworkUtil {

    /**
     * Check if there is an internet connection. If there is not, displays
     * alert dialog.
     * @param context context
     * @return Returns true if there is internet connection. Otherwise false.
     */
    public static boolean checkInternetConnection(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        boolean connection = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (connection){
            return true;
        } else {
            infoAlertDialog(context, R.string.no_internet_connection);
            return false;
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
}
