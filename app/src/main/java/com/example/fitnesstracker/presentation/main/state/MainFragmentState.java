package com.example.fitnesstracker.presentation.main.state;

import androidx.annotation.Nullable;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.models.Workout;

public record MainFragmentState(
        @Nullable PagingData<Workout> workouts
) { }
