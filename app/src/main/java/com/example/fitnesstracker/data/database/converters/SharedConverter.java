package com.example.fitnesstracker.data.database.converters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.example.fitnesstracker.domain.workout.models.Approach;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SharedConverter {
    @TypeConverter
    @Nullable public Long fromInstant(@Nullable Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.toEpochMilli();
    }

    @TypeConverter
    @Nullable
    public Instant toInstant(@Nullable Long time) {
        if (time == null) {
            return null;
        }

        return Instant.ofEpochMilli(time);
    }

    @TypeConverter
    public @NonNull String fromApproaches(List<Approach> approaches) {
        final var stringBuilder = new StringBuilder();

        approaches.forEach(approach ->
                stringBuilder
                        .append(approach.weight())
                        .append(' ')
                        .append(approach.repetitions())
                        .append(',')
        );

        return stringBuilder.toString();
    }

    @TypeConverter
    public @NonNull List<Approach> fromString(String approachString) {
        final var split = approachString.split(",");
        final var approaches = new ArrayList<Approach>();

        for (String pair: split) {
            try {
                int weight;
                int repetitions;

                final var splitPair = pair.split(" ");

                weight = Integer.parseInt(splitPair[0]);
                repetitions = Integer.parseInt(splitPair[1]);

                approaches.add(new Approach(repetitions, weight));
            } catch (Exception ignored) {

            }
        }

        return approaches;
    }
}
