package com.example.fitnesstracker.data.rest.auth;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.google.gson.annotations.SerializedName;

public record AuthResponse(
        @NonNull @SerializedName("user") UserDto userDto,
        @NonNull @SerializedName("token") String authToken
) { }
