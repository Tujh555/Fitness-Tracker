package com.example.fitnesstracker.presentation.auth.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.presentation.auth.error.AuthError;

public record AuthScreenState(
        @NonNull String login,
        @NonNull String password,
        @Nullable AuthError error
) { }
