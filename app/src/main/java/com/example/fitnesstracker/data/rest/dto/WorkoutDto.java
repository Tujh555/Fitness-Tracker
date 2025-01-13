package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public record WorkoutDto(
        @SerializedName("id") String id,
        @SerializedName("title") String title,
        @SerializedName("date") String date,
        @SerializedName("exercises") List<ExerciseDto> exercises
) {
    public @NonNull Workout toDomain() {
        final var approaches = new HashMap<String, List<Approach>>();

        final var exerciseModels = exercises
                .stream()
                .map(dto -> {
                    dto.approaches().forEach(approachDto -> {
                        final var approach = approachDto.toDomain();
                        final var list = approaches.get(approach.exerciseId());
                        if (list == null) {
                            final var newList = new ArrayList<Approach>();
                            newList.add(approach);
                            approaches.put(approach.exerciseId(), newList);
                        } else {
                            list.add(approach);
                        }
                    });

                    return dto.toDomain();
                })
                .collect(Collectors.toList());

        return new Workout(id, title, Instant.parse(date), exerciseModels, approaches);
    }

    public static @NonNull WorkoutDto of(@NonNull Workout workout) {
        final var exercises = workout
                .exercises()
                .stream()
                .map(exercise ->
                        new ExerciseDto(
                                exercise.id(),
                                exercise.title(),
                                exercise.describingPhoto(),
                                workout
                                        .approaches()
                                        .getOrDefault(exercise.id(), emptyList)
                                        .stream()
                                        .map(ApproachDto::of)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());

        return new WorkoutDto(
                workout.id(),
                workout.title(),
                workout.date().toString(),
                exercises
        );
    }

    private static final List<Approach> emptyList = new ArrayList<>(0);
}

