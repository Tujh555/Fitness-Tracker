package com.example.fitnesstracker.data.rest.auth;

import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.google.gson.annotations.SerializedName;

public record AuthResponse(
        @SerializedName("user") UserDto userDto,
        @SerializedName("token") String authToken
) { }
