package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

public record ApproachDto(
        @SerializedName("repetitions") int repetitions,
        @SerializedName("weight") int weight
) {
    @NonNull
    @Contract(" -> new")
    public Approach toDomain() {
        return new Approach(repetitions, weight);
    }
}
