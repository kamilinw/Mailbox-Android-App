package com.example.mailbox.api;

import com.example.mailbox.model.request.AddMailboxRequest;
import com.example.mailbox.model.request.ChangeEmailRequest;
import com.example.mailbox.model.request.ChangePasswordRequest;
import com.example.mailbox.model.request.UserLoginRequest;
import com.example.mailbox.model.request.UserRegisterRequest;
import com.example.mailbox.model.UserResponse;

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

    @GET("api/uzytkownik/")
    Call<UserResponse> getUserDetails(
            @Header("Authorization") String token
    );

    @POST("rejestracja")
    Call<Void> userRegister(
            @Body UserRegisterRequest userRegisterRequest
    );

    @POST("api/uzytkownik/skrzynka")
    Call<Void> addMailbox(
            @Header("Authorization") String token,
            @Body AddMailboxRequest addMailboxRequest
    );

    @POST("api/uzytkownik/email")
    Call<UserResponse> changeEmail(
            @Header("Authorization") String token,
            @Body ChangeEmailRequest changeEmailRequest
    );

    @POST("api/uzytkownik/haslo")
    Call<UserResponse> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest changePasswordRequest
    );

    @DELETE("api/uzytkownik/skrzynka/{id}")
    Call<UserResponse> deleteMailbox(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

}
