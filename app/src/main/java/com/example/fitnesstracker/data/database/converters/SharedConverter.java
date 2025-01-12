package com.example.fitnesstracker.data.database.converters;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.time.Instant;

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
}
