package com.example.fitnesstracker.data.rest.auth;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public record LoginRequest(
        @Nullable @SerializedName("login") String login,
        @Nullable @SerializedName("password") String password
) { }
