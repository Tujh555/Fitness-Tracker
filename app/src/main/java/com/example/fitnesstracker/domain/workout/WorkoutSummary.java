package com.example.fitnesstracker.domain.workout;

import androidx.annotation.NonNull;

public record WorkoutSummary(
        @NonNull String date,
        float tonnage
) { }
