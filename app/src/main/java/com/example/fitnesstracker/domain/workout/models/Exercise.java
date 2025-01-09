package com.example.fitnesstracker.domain.workout.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public record Exercise(
        @NonNull String id,
        @NonNull String title,
        @Nullable String describingPhoto,
        @NonNull List<Approach> approaches
) { }
