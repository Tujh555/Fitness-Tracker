package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RemoteKeys {
    @PrimaryKey @ColumnInfo(name = "workout_id") @NonNull
    public String workoutId;
    public Integer prev;
    public Integer next;

    public RemoteKeys(@NonNull String workoutId, Integer prev, Integer next) {
        this.workoutId = workoutId;
        this.prev = prev;
        this.next = next;
    }
}
