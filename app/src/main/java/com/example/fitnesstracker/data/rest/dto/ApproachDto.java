package com.example.fitnesstracker.data.rest.dto;

import com.google.gson.annotations.SerializedName;

public record ApproachDto(
        @SerializedName("repetitions") int repetitions,
        @SerializedName("weight") int weight
) {
}
