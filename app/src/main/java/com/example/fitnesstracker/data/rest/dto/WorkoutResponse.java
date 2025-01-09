package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record WorkoutResponse(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("date") String date,
        @SerializedName("exercises") List<ExerciseResponse> exercises
) {
    public @NonNull Workout toDomain() {
        final var exerciseModels = exercises
                .stream()
                .map(e -> new Exercise(
                        e.id(),
                        e.title(),
                        e.describingPhoto(),
                        e.approaches()
                                .stream()
                                .map(a -> new Approach(a.id(), a.repetitions(), a.weight()))
                                .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());

        return new Workout(id, title, Instant.parse(date), exerciseModels);
    }
}

record ExerciseResponse(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("describingPhoto") String describingPhoto,
        @SerializedName("approaches") List<ApproachResponse> approaches
) {
}

record ApproachResponse(
        @SerializedName("id") String id,
        @SerializedName("repetitions") int repetitions,
        @SerializedName("weight") int weight
) {
}