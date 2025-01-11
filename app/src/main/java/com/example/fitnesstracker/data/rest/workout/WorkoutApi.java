package com.example.fitnesstracker.data.rest.workout;


import com.example.fitnesstracker.data.rest.dto.ExerciseDto;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
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
    Single<ExerciseDto> putExerciseDescription(@Path("id") String id, @Part MultipartBody.Part photo);
}
