package com.example.mailbox.util;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mailbox.api.MailboxRetrofitClient;
import com.example.mailbox.data.MailboxDatabase;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.model.Mailbox;
import com.example.mailbox.model.UserResponse;
import com.example.mailbox.ui.mailbox.MailboxActivity;
import com.example.mailbox.ui.mailbox.MailboxListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserUtil {

    public static void logoutUser(Context context){
        MailboxDatabase mailboxDatabase = MailboxDatabase.getInstance(context);
        mailboxDatabase.resetDatabase();

        UserDatabase userDatabase = UserDatabase.getInstance(context);
        userDatabase.resetDatabase();
    }

    public static <T extends BaseAdapter> void downloadUserData(Context context, boolean isLoggingIn, @Nullable T adapter){
        UserDatabase db = UserDatabase.getInstance(context);
        String token = db.getJwtToken();
        if (token == null)
            return;

        Call<UserResponse> call = MailboxRetrofitClient
                .getInstance(token).getApi().getUserDetails(token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() != 200) {
                    // TODO handle errors
                    Toast.makeText(context, "Response code: " + response.code(), Toast.LENGTH_LONG).show();

                    return;
                }

                // TODO Authentication Header



                UserResponse userResponse = response.body();

                // save data to database
                MailboxDatabase mailboxDatabase = MailboxDatabase.getInstance(context);
                List<Long> mailboxIds = new ArrayList<>();
                for (Mailbox mailbox: userResponse.getMailboxes() ) {
                    mailboxIds.add(mailbox.getMailboxId());
                    mailboxDatabase.saveMailbox(mailbox);
                }
                UserDatabase userDatabase = UserDatabase.getInstance(context);


                String token = response.headers().get("Authorization");
                if (token != null){
                    userDatabase.saveJWT(userResponse.getUsername(),token);
                }

                userDatabase.saveUser(
                        userResponse.getUsername(),
                        userResponse.getEmail(),
                        mailboxIds
                );
                //Toast.makeText(context, "success! " + userResponse.getMailboxes().get(0).getBattery(), Toast.LENGTH_LONG).show();
                userDatabase.close();
                mailboxDatabase.close();

                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }

                if (isLoggingIn){
                    Intent intent = new Intent(context, MailboxActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(context, "Nie udało się pobrać danych!", Toast.LENGTH_LONG).show();

            }
        });


    }
}
