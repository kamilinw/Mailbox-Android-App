package com.example.mailbox.api;

import com.example.mailbox.model.UserLoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("login")
    Call<Void> userLogin(
            @Body UserLoginRequest userLoginRequest
    );
}
