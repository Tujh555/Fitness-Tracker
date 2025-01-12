package com.example.fitnesstracker.domain.workout.models;

import androidx.annotation.NonNull;

public record Approach(
        @NonNull String id,
        @NonNull String exerciseId,
        @NonNull String workoutId,
        int repetitions,
        int weight
) { }
