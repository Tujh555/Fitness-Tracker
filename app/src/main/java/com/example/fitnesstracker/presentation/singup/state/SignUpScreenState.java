package com.example.fitnesstracker.presentation.singup.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record SignUpScreenState(
        @NonNull String login,
        @NonNull String password,
        @Nullable String age,
        @Nullable String name,
        boolean isError
) {
    public static final SignUpScreenState empty = new SignUpScreenState("", "", "", "", false);
}
