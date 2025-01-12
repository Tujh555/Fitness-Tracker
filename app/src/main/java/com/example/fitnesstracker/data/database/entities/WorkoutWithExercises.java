package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkoutWithExercises {
    @Embedded
    public WorkoutEntity workout;
    @Relation(
            parentColumn = "id",
            entityColumn = "exercise_id",
            associateBy = @Junction(WorkoutCrossRef.class)
    )
    public List<ExerciseEntity> exercises;

    public @NonNull Workout toDomain(@NonNull Map<String, List<Approach>> approaches) {
        return new Workout(
                workout.id,
                workout.title,
                workout.date,
                exercises
                        .stream()
                        .map(exercise ->
                                new Exercise(
                                        exercise.exerciseId,
                                        exercise.title,
                                        exercise.describingPhoto
                                )
                        ).collect(Collectors.toList()),
                approaches
        );
    }
}
