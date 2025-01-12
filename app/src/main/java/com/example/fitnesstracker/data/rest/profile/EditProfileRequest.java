package com.example.fitnesstracker.data.rest.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record EditProfileRequest(
        @NonNull String name,
        @NonNull String login,
        @Nullable Integer age,
        @NonNull String target
) { }
