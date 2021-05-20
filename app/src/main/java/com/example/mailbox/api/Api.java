package com.example.mailbox.api;

import com.example.mailbox.model.UserLoginRequest;
import com.example.mailbox.model.UserRegisterRequest;
import com.example.mailbox.model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @POST("login")
    Call<Void> userLogin(
            @Body UserLoginRequest userLoginRequest
    );

    @GET("api/user/")
    Call<UserResponse> getUserDetails(
            @Header("Authorization") String token
    );

    @POST("registration")
    Call<Void> userRegister(
            @Body UserRegisterRequest userRegisterRequest
    );

}
