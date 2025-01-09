package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutWithExercises {
    @NonNull
    @Embedded
    public final WorkoutEntity workout;
    @Relation(
            parentColumn = "id",
            entityColumn = "workout_id"
    )
    @NonNull
    public final List<ExerciseWithApproaches> exercises;

    public WorkoutWithExercises(
            @NonNull WorkoutEntity workout,
            @NonNull List<ExerciseWithApproaches> exercises
    ) {
        this.workout = workout;
        this.exercises = exercises;
    }

    public @NonNull Workout toDomain() {
        return new Workout(
                workout.id,
                workout.title,
                workout.date,
                exercises.stream().map(exercise -> {
                            final var approaches = exercise.approaches
                                    .stream()
                                    .map(approach -> new Approach(approach.id, approach.repetitions, approach.weight))
                                    .collect(Collectors.toList());

                            return new Exercise(
                                    exercise.exercise.id,
                                    exercise.exercise.title,
                                    exercise.exercise.describingPhoto,
                                    approaches
                            );
                        }
                ).collect(Collectors.toList())
        );
    }
}
