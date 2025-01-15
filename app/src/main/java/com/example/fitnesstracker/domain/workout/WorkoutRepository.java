package com.example.fitnesstracker.domain.workout;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    @NonNull
    Completable createWorkout(
            @NonNull String title,
            @NonNull Instant date,
            @NonNull List<Exercise> exercises,
            @NonNull Map<String, List<Pair<Integer, Integer>>> approaches
    );

    @NonNull
    Completable editWorkout(
            @NonNull String id,
            @NonNull String title,
            @NonNull Instant date,
            @NonNull List<Exercise> exercises,
            @NonNull Map<String, List<Pair<Integer, Integer>>> approaches
    );
}
