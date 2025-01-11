package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"id", "exercise_id"})
public class WorkoutCrossRef {
    @NonNull
    public String id;
    @NonNull
    @ColumnInfo(name = "exercise_id", index = true)
    public String exerciseId;

    public WorkoutCrossRef(@NonNull String id, @NonNull String exerciseId) {
        this.id = id;
        this.exerciseId = exerciseId;
    }
}
