package com.example.fitnesstracker.presentation.workout.create.action;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.Instant;

public interface WorkoutAppendScreenAction {
    class Clear implements WorkoutAppendScreenAction {}

    class Save implements WorkoutAppendScreenAction {}

    record Title(@NonNull String text) implements WorkoutAppendScreenAction {}

    record AppendExercise(@NonNull Exercise exercise, int repetitions, int weight) implements WorkoutAppendScreenAction {}

    record ApplyExisting(@NonNull Workout workout) implements WorkoutAppendScreenAction {}

    record SetDate(@NonNull Instant time) implements WorkoutAppendScreenAction {}
}
