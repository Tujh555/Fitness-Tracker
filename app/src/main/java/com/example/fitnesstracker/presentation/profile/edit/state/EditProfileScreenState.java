package com.example.fitnesstracker.presentation.profile.edit.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record EditProfileScreenState(
        @Nullable String avatar,
        @NonNull String name,
        @NonNull String login,
        @NonNull String age,
        @NonNull String target
) {
    public static @NonNull EditProfileScreenState empty = new EditProfileScreenState(
            null, "", "", "", ""
    );
}
