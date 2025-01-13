package com.example.fitnesstracker.presentation.exercise.edit.state;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record ExerciseEditScreenState(
        @Nullable String existingId,
        @Nullable Uri uri,
        @NonNull String title
) {

}
