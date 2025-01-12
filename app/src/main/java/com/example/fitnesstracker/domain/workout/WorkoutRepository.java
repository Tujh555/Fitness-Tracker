package com.example.fitnesstracker.domain.workout;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface WorkoutRepository {
    @NonNull
    Flowable<PagingData<Workout>> observeWorkouts();

    @NonNull
    Flowable<List<WorkoutSummary>> observeSummary();

    @NonNull
    Flowable<List<Exercise>> observeExercises();

    @NonNull
    Completable putExerciseDescription(
            @NonNull String id,
            @Nullable Uri uri,
            @NonNull String title
    );

    @NonNull
    Completable createExercise(@Nullable Uri uri, @NonNull String title);
}
