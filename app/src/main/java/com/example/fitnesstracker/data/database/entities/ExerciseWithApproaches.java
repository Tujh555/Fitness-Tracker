package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ExerciseWithApproaches {
    @NonNull
    @Embedded
    public final ExerciseEntity exercise;
    @Relation(
            parentColumn = "id",
            entityColumn = "exercise_id"
    )
    @NonNull
    public final List<ApproachEntity> approaches;

    public ExerciseWithApproaches(@NonNull ExerciseEntity exercise, @NonNull List<ApproachEntity> approaches) {
        this.exercise = exercise;
        this.approaches = approaches;
    }
}
