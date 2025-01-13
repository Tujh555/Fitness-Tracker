package com.example.fitnesstracker.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.fitnesstracker.domain.workout.models.Approach;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ApproachEntity {
    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "exercise_id")
    @NonNull
    public String exerciseId;
    @ColumnInfo(name = "workout_id")
    @NonNull
    public String workoutId;
    public int weight;
    public int repetitions;

    public ApproachEntity(
            @NonNull String id,
            @NonNull String exerciseId,
            @NonNull String workoutId,
            int weight,
            int repetitions
    ) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.repetitions = repetitions;
        this.workoutId = workoutId;
    }

    @NonNull
    public Approach toDomain() {
        return new Approach(id, exerciseId, workoutId, repetitions, weight);
    }

    @Contract("_ -> new")
    @NonNull
    public static ApproachEntity toDb(@NonNull Approach approach) {
        return new ApproachEntity(
                approach.id(),
                approach.exerciseId(),
                approach.workoutId(),
                approach.weight(),
                approach.repetitions()
        );
    }

    @NonNull
    public static Map<String, List<Approach>> associate(List<ApproachEntity> approaches) {
        final var map = new HashMap<String, List<Approach>>();

        approaches.forEach(approachEntity -> {
            final var approach = approachEntity.toDomain();
            final var list = map.get(approach.exerciseId());

            if (list == null) {
                final var newList = new ArrayList<Approach>();
                newList.add(approach);
                map.put(approach.exerciseId(), newList);
            } else {
                list.add(approach);
            }
        });

        return map;
    }
}
