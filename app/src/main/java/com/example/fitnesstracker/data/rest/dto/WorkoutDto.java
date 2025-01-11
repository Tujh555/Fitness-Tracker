package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Workout;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record WorkoutDto(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("date") String date,
        @SerializedName("exercises") List<ExerciseDto> exercises
) {
    public @NonNull Workout toDomain() {
        final var exerciseModels = exercises
                .stream()
                .map(ExerciseDto::toDomain)
                .collect(Collectors.toList());

        return new Workout(id, title, Instant.parse(date), exerciseModels);
    }
}

