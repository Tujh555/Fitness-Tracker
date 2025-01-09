package com.example.fitnesstracker.data.database;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnesstracker.data.database.entities.ApproachEntity;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;

import java.time.Instant;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkouts(List<WorkoutEntity> workoutEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertExercises(List<ExerciseEntity> exercises);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertApproach(List<ApproachEntity> approaches);

    @Query("DELETE FROM WorkoutEntity")
    Completable clear();

    @Query("SELECT * FROM WorkoutEntity ORDER BY date DESC")
    PagingSource<Instant, WorkoutWithExercises> selectAll();
}
