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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkoutApi {

    @GET("workouts")
    Single<List<WorkoutDto>> getWorkouts();

    @Multipart
    @POST("exercises")
    Single<ExerciseDto> putExercise(
            @Part MultipartBody.Part photoBody,
            @Part("title") RequestBody titleBody,
            @Part("id") RequestBody idBody
    );

    @GET("summary")
    Single<List<SummaryDto>> getSummary();

    @GET("exercises/all")
    Single<List<ExerciseDto>> getExercises();

    @POST("workouts")
    Single<WorkoutDto> createWorkout(@Body WorkoutDto workout);

    @PUT("workouts")
    Single<WorkoutDto> editWorkout(@Body WorkoutDto workout);
}
