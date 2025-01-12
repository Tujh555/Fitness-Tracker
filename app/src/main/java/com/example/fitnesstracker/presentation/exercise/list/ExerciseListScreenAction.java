package com.example.fitnesstracker.presentation.exercise.list;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Exercise;

public interface ExerciseListScreenAction {
    record Edit(@NonNull Exercise exercise) implements ExerciseListScreenAction {}

    class Create implements ExerciseListScreenAction {}
}
