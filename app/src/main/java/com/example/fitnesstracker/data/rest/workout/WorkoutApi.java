package com.example.fitnesstracker.data.rest.workout;


import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WorkoutApi {

    @GET("workouts")
    Single<WorkoutPage> getWorkouts(
            @Query("page") int page,
            @Query("size") int pageSize
    );
}
