package com.example.fitnesstracker.data.rest.workout;


import com.example.fitnesstracker.data.rest.dto.ExerciseDto;
import com.example.fitnesstracker.data.rest.dto.SummaryDto;
import com.example.fitnesstracker.data.rest.dto.WorkoutDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkoutApi {

    @GET("workouts")
    Single<WorkoutPage> getWorkouts(
            @Query("page") int page,
            @Query("size") int pageSize
    );

    @Multipart
    @POST("exercises/{id}")
    Single<ExerciseDto> putExercise(
            @Path("id") String id,
            @Part("photo") MultipartBody.Part photo,
            @Part("title") RequestBody title
    );

    @GET("summary")
    Single<List<SummaryDto>> getSummary();

    @GET("exercises/all")
    Single<List<ExerciseDto>> getExercises();

    @POST("workouts")
    Completable createWorkout(@Body WorkoutDto workout);
}
