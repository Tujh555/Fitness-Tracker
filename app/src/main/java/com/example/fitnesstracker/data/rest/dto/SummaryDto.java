package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.WorkoutSummary;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

public record SummaryDto(
        @NonNull @SerializedName("date") String date,
        @SerializedName("tonnage") float tonnage
) {
    @NonNull
    @Contract(" -> new")
    public WorkoutSummary toDomain() {
        return new WorkoutSummary(date, tonnage);
    }
}
