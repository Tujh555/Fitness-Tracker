package com.example.fitnesstracker.domain.workout;

import androidx.annotation.NonNull;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface WorkoutRepository {
    @NonNull
    Flowable<PagingData<Workout>> observeWorkouts();
}
