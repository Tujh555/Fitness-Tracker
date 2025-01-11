package com.example.fitnesstracker.data.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Flowable;

public interface Storage<T> {
    void save(@NonNull T item);

    @Nullable T get();

    void clear();
}
