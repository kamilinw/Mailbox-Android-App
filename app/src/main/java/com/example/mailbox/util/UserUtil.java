package com.example.mailbox.util;

import android.content.Context;

import com.example.mailbox.data.Database;

public class UserUtil {

    public static void logoutUser(Context context){
        Database db = Database.getInstance(context);
        db.resetDatabase(context);
    }
}
