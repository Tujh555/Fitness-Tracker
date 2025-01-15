package com.example.fitnesstracker.presentation.main.action;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Workout;

public interface MainPageScreenAction {
    record Edit(@NonNull Workout workout) implements MainPageScreenAction {}
}
