package com.example.fitnesstracker.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Storage<T> {
    void save(@NonNull T item);

    @Nullable T get();

    void clear();
}
