package com.example.fitnesstracker.domain.workout.models;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record Workout(
        @NonNull String id,
        @NonNull String title,
        @NonNull Instant date,
        @NonNull List<Exercise> exercises,
        @NonNull Map<String, List<Approach>> approaches
) { }
