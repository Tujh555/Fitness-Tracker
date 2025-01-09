package com.example.fitnesstracker.data.rest.auth;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public record LoginRequest(
        @NonNull @SerializedName("login") String login,
        @NonNull @SerializedName("password") String password
) { }
