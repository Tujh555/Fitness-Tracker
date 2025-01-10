package com.example.fitnesstracker.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnesstracker.data.database.entities.RemoteKeys;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RemoteKeys> keys);

    @Query("SELECT * FROM RemoteKeys WHERE workout_id = :movieId")
    RemoteKeys resolve(String movieId);

    @Query("DELETE FROM RemoteKeys")
    Completable clear();
}
