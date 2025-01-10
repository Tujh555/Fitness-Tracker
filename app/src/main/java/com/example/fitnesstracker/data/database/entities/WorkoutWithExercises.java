package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutWithExercises {
    @Embedded
    public WorkoutEntity workout;
    @Relation(
            parentColumn = "id",
            entityColumn = "workout_id"
    )
    public List<ExerciseEntity> exercises;

    public @NonNull Workout toDomain() {
        return new Workout(
                workout.id,
                workout.title,
                workout.date,
                exercises
                        .stream()
                        .map(exercise ->
                                new Exercise(
                                        exercise.id,
                                        exercise.title,
                                        exercise.describingPhoto,
                                        exercise.approaches
                                )
                        ).collect(Collectors.toList())
        );
    }
}
