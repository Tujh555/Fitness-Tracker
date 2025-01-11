package com.example.fitnesstracker.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fitnesstracker.data.database.converters.SharedConverter;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.RemoteKeys;
import com.example.fitnesstracker.data.database.entities.WorkoutCrossRef;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Database(
        version = 1,
        entities = {
                WorkoutEntity.class,
                ExerciseEntity.class,
                RemoteKeys.class,
                WorkoutCrossRef.class
        },
        exportSchema = false
)
@TypeConverters(SharedConverter.class)
public abstract class WorkoutDatabase extends RoomDatabase {
    public abstract @NonNull WorkoutDao workoutDao();

    public abstract @NonNull RemoteKeysDao remoteKeysDao();

    @Module
    @InstallIn(SingletonComponent.class)
    public static class Provider {
        @Provides
        @Singleton
        WorkoutDatabase provide(@ApplicationContext Context context) {
            return Room
                    .databaseBuilder(
                            context,
                            WorkoutDatabase.class,
                            WorkoutDatabase.class.getSimpleName()
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        @Provides
        @Singleton
        WorkoutDao provideDao(@NonNull WorkoutDatabase database) {
            return database.workoutDao();
        }
    }
}
