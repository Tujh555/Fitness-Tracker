package com.example.fitnesstracker.data.database;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkouts(List<WorkoutEntity> workoutEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertExercises(List<ExerciseEntity> exercises);

    @Query("DELETE FROM WorkoutEntity")
    Completable clear();

    @Transaction
    @Query("SELECT * FROM WorkoutEntity")
    PagingSource<Integer, WorkoutWithExercises> selectAll();
}
