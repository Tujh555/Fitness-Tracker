package com.example.fitnesstracker.data.rest.workout;


import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.rest.dto.WorkoutResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WorkoutApi {

    @GET("workouts")
    Single<List<WorkoutResponse>> getWorkoutsAfter(
            @Nullable @Query("after") String date,
            @Query("size") int pageSize
    );

    @GET("workouts")
    Single<List<WorkoutResponse>> getWorkoutsBefore(
            @Nullable @Query("before") String date,
            @Query("size") int pageSize
    );
}
