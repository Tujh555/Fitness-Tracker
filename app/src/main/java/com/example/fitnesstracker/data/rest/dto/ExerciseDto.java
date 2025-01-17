package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.stream.Collectors;

public record ExerciseDto(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("describing_photo") @Nullable String describingPhoto,
        @SerializedName("approaches") List<ApproachDto> approaches
) {
    @NonNull
    @Contract(" -> new")
    public Exercise toDomain() {
        return new Exercise(id, title, describingPhoto);
    }

    @NonNull
    public ExerciseEntity toDb() {
        return new ExerciseEntity(id, title, describingPhoto);
    }
}
