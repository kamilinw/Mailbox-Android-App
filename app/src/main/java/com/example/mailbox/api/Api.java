package com.example.mailbox.api;

import com.example.mailbox.model.AddMailboxRequest;
import com.example.mailbox.model.UserLoginRequest;
import com.example.mailbox.model.UserRegisterRequest;
import com.example.mailbox.model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @POST("api/user/mailbox")
    Call<Void> addMailbox(
            @Header("Authorization") String token,
            @Body AddMailboxRequest addMailboxRequest
    );

    @DELETE("api/user/mailbox/{id}")
    Call<UserResponse> deleteMailbox(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

}
