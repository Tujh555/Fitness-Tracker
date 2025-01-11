package com.example.fitnesstracker.domain.workout;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface WorkoutRepository {
    @NonNull
    Flowable<PagingData<Workout>> observeWorkouts();

    Single<Exercise> putExerciseDescription(@NonNull String id, @NonNull Uri uri);
}
