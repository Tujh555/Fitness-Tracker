package com.example.fitnesstracker.data.rest.auth;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/auth/login")
    Single<AuthResponse> login(@Body LoginRequest request);

    @POST("/auth/register")
    Single<AuthResponse> register(@Body RegisterRequest request);

    @POST("/auth/logout")
    Completable logout(@Body LogoutRequest request);
}
