package com.example.fitnesstracker.data.rest.auth;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public record LogoutRequest(@NonNull @SerializedName("id") Integer id) { }
