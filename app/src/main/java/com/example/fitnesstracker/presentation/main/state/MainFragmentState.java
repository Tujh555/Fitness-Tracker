package com.example.fitnesstracker.presentation.main.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.workout.WorkoutSummary;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;

public record MainFragmentState(
        @Nullable List<Workout> workouts,
        @NonNull List<WorkoutSummary> summaries
) { }
