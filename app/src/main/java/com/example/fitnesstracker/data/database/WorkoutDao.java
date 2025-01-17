package com.example.fitnesstracker.data.database;

import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fitnesstracker.data.database.entities.ApproachEntity;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutCrossRef;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM WorkoutEntity")
    Flowable<List<WorkoutWithExercises>> selectWorkouts();

    @Query("SELECT * FROM ApproachEntity")
    Flowable<List<ApproachEntity>> selectApproaches();

    @Query("SELECT * FROM ExerciseEntity")
    Flowable<List<ExerciseEntity>> selectExercises();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkouts(List<WorkoutEntity> workoutEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertExercises(List<ExerciseEntity> exercises);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCrossRef(List<WorkoutCrossRef> refs);

    @Query("UPDATE ExerciseEntity SET describingPhoto = :photo WHERE exercise_id = :id")
    Completable putExercisePhoto(String id, String photo);

    @Query("DELETE FROM WorkoutEntity")
    Completable clearWorkouts();

    @Query("DELETE FROM EXERCISEENTITY")
    Completable clearExercises();

    @Query("DELETE FROM ApproachEntity")
    Completable clearApproaches();

    @Query("DELETE FROM WorkoutCrossRef")
    Completable clearCrossRef();

    @Transaction
    default void clear() {
        clearWorkouts()
                .andThen(clearExercises())
                .andThen(clearApproaches())
                .andThen(clearCrossRef())
                .blockingAwait();
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertApproaches(List<ApproachEntity> approaches);

    @Query("DELETE FROM ApproachEntity WHERE workout_id = :id")
    Completable clearApproachesForWorkout(String id);
}
