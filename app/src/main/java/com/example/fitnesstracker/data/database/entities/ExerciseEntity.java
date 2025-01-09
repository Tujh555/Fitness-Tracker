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
        public final String id;

        @NonNull
        @ColumnInfo(name = "workout_id")
        public final String workoutId;

        @NonNull
        public final String title;

        @Nullable
        public final String describingPhoto;

    public ExerciseEntity(
            @NonNull String id,
            @NonNull String workoutId,
            @NonNull String title,
            @Nullable String describingPhoto
    ) {
        this.id = id;
        this.workoutId = workoutId;
        this.title = title;
        this.describingPhoto = describingPhoto;
    }

    public @NonNull Exercise toDomain(@NonNull List<Approach> approaches) {
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
                exercise.describingPhoto()
        );
    }
}
