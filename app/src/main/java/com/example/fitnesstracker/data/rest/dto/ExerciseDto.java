package com.example.fitnesstracker.data.rest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record ExerciseDto(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("describingPhoto") String describingPhoto,
        @SerializedName("approaches") List<ApproachDto> approaches
) {
}
