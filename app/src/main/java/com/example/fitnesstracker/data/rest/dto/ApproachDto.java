package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

public record ApproachDto(
        @SerializedName("id") String id,
        @SerializedName("workout_id") String workoutId,
        @SerializedName("exercise_id") String exerciseId,
        @SerializedName("repetitions") int repetitions,
        @SerializedName("weight") int weight
) {
    @NonNull
    @Contract(" -> new")
    public Approach toDomain() {
        return new Approach(id, exerciseId, workoutId, repetitions, weight);
    }

    @NonNull
    @Contract("_ -> new")
    public static ApproachDto of(@NonNull Approach approach) {
        return new ApproachDto(
                approach.id(),
                approach.workoutId(),
                approach.exerciseId(),
                approach.repetitions(),
                approach.weight()
        );
    }
}
