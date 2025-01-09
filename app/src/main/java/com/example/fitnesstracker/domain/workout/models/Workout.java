package com.example.fitnesstracker.domain.workout.models;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.util.List;

public record Workout(
        @NonNull String id,
        @NonNull String title,
        @NonNull Instant date,
        @NonNull List<Exercise> exercises
) { }
