package com.example.fitnesstracker.data.rest.workout;

import com.example.fitnesstracker.data.rest.dto.WorkoutDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public record WorkoutPage(
        @SerializedName("total_pages") Integer totalPages,
        @SerializedName("page") Integer page,
        @SerializedName("workouts") List<WorkoutDto> workouts
) {}
