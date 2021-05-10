package com.example.mailbox.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailbox.R;
import com.example.mailbox.api.AuthRetrofitClient;
import com.example.mailbox.model.UserLoginRequest;
import com.example.mailbox.util.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        loadingProgressBar = findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        context = this;
    }

    public void loginOnClick(View view) {
        if (!isDataCorrect() || !NetworkUtil.checkInternetConnection(LoginActivity.this)){
            return;
        }

        //disable views
        enableViews(false);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        UserLoginRequest userLoginRequest = new UserLoginRequest(username, password);

        //launch a task to get the JWT from the server
        Call<Void> call = AuthRetrofitClient
                .getInstance().getApi().userLogin(userLoginRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //ErrorResponse errorResponse = response.body();
                if (response.code() != 200) {
                    Toast.makeText(LoginActivity.this, "Response code: " + response.code(), Toast.LENGTH_LONG).show();
                    enableViews(true);
                    return;
                }

                // TODO save token to db
                Toast.makeText(LoginActivity.this, response.headers().get("Authorization"), Toast.LENGTH_LONG).show();
                enableViews(true);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.login_failed_toast, Toast.LENGTH_LONG).show();
                enableViews(true);
            }
        });
        Toast.makeText(getApplicationContext(), "Logging in!", Toast.LENGTH_SHORT).show();
    }

    private boolean isDataCorrect() {
        boolean dataCorrect = true;

        //email
        TextView textViewWrongEmail = findViewById(R.id.textViewWrongEmail);
        if (usernameEditText.getText().toString().equals("")){
            textViewWrongEmail.setVisibility(View.VISIBLE);
            dataCorrect = false;
        } else {
            textViewWrongEmail.setVisibility(View.GONE);
        }

        // password
        TextView textViewEnterPassword = findViewById(R.id.textViewEnterPassword);
        if (passwordEditText.getText().toString().equals("")){
            textViewEnterPassword.setVisibility(View.VISIBLE);
            dataCorrect = false;
        } else {
            textViewEnterPassword.setVisibility(View.GONE);
        }

        return dataCorrect;
    }

    public void enableViews(Boolean isActive){
        usernameEditText.setEnabled(isActive);
        passwordEditText.setEnabled(isActive);
        loginButton.setEnabled(isActive);
        if (isActive){
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }else{
            loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

}

