package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;

import java.util.List;

@Entity(
        foreignKeys = @ForeignKey(
                entity = WorkoutEntity.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class ExerciseEntity {
        @PrimaryKey
        @NonNull
        public String id;
        @ColumnInfo(name = "workout_id")
        public String workoutId;
        public String title;
        public String describingPhoto;
        public List<Approach> approaches;

    public ExerciseEntity(
            @NonNull String id,
            @NonNull String workoutId,
            @NonNull String title,
            @Nullable String describingPhoto,
            @NonNull List<Approach> approaches
    ) {
        this.id = id;
        this.workoutId = workoutId;
        this.title = title;
        this.describingPhoto = describingPhoto;
        this.approaches = approaches;
    }

    public @NonNull Exercise toDomain() {
        return new Exercise(id, title, describingPhoto, approaches);
    }

    public static @NonNull ExerciseEntity toDb(
            @NonNull String workoutId,
            @NonNull Exercise exercise
    ) {
        return new ExerciseEntity(
                exercise.id(),
                workoutId,
                exercise.title(),
                exercise.describingPhoto(),
                exercise.approaches()
        );
    }
}
