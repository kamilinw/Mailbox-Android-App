package com.example.mailbox.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mailbox.api.MailboxRetrofitClient;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.model.Mailbox;
import com.example.mailbox.model.UserResponse;
import com.example.mailbox.ui.mailbox.MailboxActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserUtil {

    public static void logoutUser(Context context){
        UserDatabase db = UserDatabase.getInstance(context);
        db.resetDatabase(context);
    }

    public static void downloadUserData(Context context, boolean isLoggingIn){
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

                UserResponse userResponse = response.body();



                // TODO save data to database
                List<Long> mailboxIds = new ArrayList<>();
                for (Mailbox mailbox: userResponse.getMailboxes() ) {
                    mailboxIds.add(mailbox.getMailboxId());
                }

                UserDatabase userDatabase = UserDatabase.getInstance(context);
                userDatabase.saveUser(
                        userResponse.getUsername(),
                        userResponse.getEmail(),
                        mailboxIds
                );



                Toast.makeText(context, "success! " + userDatabase.getMailboxIds().toString() , Toast.LENGTH_LONG).show();
                userDatabase.close();

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
