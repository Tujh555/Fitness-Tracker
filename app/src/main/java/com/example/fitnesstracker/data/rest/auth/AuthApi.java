package com.example.fitnesstracker.data.rest.auth;

import com.example.fitnesstracker.data.rest.dto.UserDto;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth/login")
    Single<UserDto> login(@Body LoginRequest request);

    @POST("auth/register")
    Single<UserDto> register(@Body RegisterRequest request);
}
