package com.example.fitnesstracker.data.rest.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public record RegisterRequest(
        @NonNull @SerializedName("login") String login,
        @NonNull @SerializedName("password") String password,
        @Nullable @SerializedName("age") Integer age,
        @Nullable @SerializedName("name") String name
) { }
