package com.example.fitnesstracker.presentation.profile.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record ProfileScreenState(
        @Nullable String avatar,
        @NonNull String name,
        @NonNull String login,
        @NonNull String age,
        @Nullable String target,
        boolean edits
) {
    public static @NonNull ProfileScreenState empty = new ProfileScreenState(
            null, "", "", "", "", false
    );
}
