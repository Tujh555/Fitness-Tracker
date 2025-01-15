package com.example.fitnesstracker.presentation.workout.create.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record WorkoutAppendScreenState(
        @Nullable String existingId,
        @NonNull String title,
        @Nullable Instant date,
        @NonNull List<Exercise> availableExercises,
        @NonNull List<Pair<Exercise, List<Pair<Integer, Integer>>>> selectedExercises
) {
    public static final WorkoutAppendScreenState empty = new WorkoutAppendScreenState(
            null,
            "",
            null,
            new ArrayList<>(),
            new ArrayList<>()
    );
}
