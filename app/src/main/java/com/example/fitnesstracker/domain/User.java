package com.example.fitnesstracker.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record User(
        @NonNull String id,
        @NonNull String name,
        @NonNull String login,
        @Nullable Integer age,
        @Nullable String avatar,
        @Nullable String target
) { }