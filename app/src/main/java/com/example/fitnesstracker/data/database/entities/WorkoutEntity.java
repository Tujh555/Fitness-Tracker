package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.fitnesstracker.domain.workout.models.Approach;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
public class WorkoutEntity {
    @PrimaryKey
    @NonNull
    public final String id;
    @NonNull
    public final String title;
    @NonNull
    public final Instant date;

    public WorkoutEntity(@NonNull String id, @NonNull String title, @NonNull Instant date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public @NonNull Workout toDomain(
            @NonNull List<Exercise> exercises,
            @NonNull Map<String, List<Approach>> approaches
    ) {
        return new Workout(id, title, date, exercises, approaches);
    }

    public static @NonNull WorkoutEntity toDb(@NonNull Workout workout) {
        return new WorkoutEntity(
                workout.id(),
                workout.title(),
                workout.date()
        );
    }
}
