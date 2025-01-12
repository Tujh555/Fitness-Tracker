package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.fitnesstracker.domain.workout.models.Exercise;

@Entity
public class ExerciseEntity {
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "exercise_id")
        public String exerciseId;
        public String title;
        public String describingPhoto;

    public ExerciseEntity(
            @NonNull String exerciseId,
            @NonNull String title,
            @Nullable String describingPhoto
    ) {
        this.exerciseId = exerciseId;
        this.title = title;
        this.describingPhoto = describingPhoto;
    }

    public @NonNull Exercise toDomain() {
        return new Exercise(exerciseId, title, describingPhoto);
    }

    public static @NonNull ExerciseEntity toDb(
            @NonNull Exercise exercise
    ) {
        return new ExerciseEntity(
                exercise.id(),
                exercise.title(),
                exercise.describingPhoto()
        );
    }
}
