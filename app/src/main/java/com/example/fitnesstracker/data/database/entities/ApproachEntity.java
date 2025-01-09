package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.fitnesstracker.domain.workout.models.Approach;

@Entity(
        foreignKeys = @ForeignKey(
                entity = ExerciseEntity.class,
                parentColumns = "id",
                childColumns = "exercise_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class ApproachEntity {
        @PrimaryKey
        @NonNull
        public final String id;

        @NonNull
        @ColumnInfo(name = "exercise_id")
        public final String exerciseId;

        public final int repetitions;

        public final int weight;

    public ApproachEntity(@NonNull String id, @NonNull String exerciseId, int repetitions, int weight) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.repetitions = repetitions;
        this.weight = weight;
    }

    public @NonNull Approach toDomain() {
        return new Approach(id, repetitions, weight);
    }

    public static @NonNull ApproachEntity toDb(
            @NonNull String exerciseId,
            @NonNull Approach approach
    ) {
        return new ApproachEntity(approach.id(), exerciseId, approach.repetitions(), approach.weight());
    }
}
