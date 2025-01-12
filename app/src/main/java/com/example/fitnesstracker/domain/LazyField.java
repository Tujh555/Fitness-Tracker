package com.example.fitnesstracker.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

public final class LazyField<T> {
    private final @NonNull Supplier<T> initializer;
    private @Nullable T value;

    private LazyField(@NonNull Supplier<T> initializer) {
        this.initializer = initializer;
    }

    @NonNull public synchronized T get() {
        if (value != null) {
            return value;
        }
        final var created = initializer.get();
        value = created;
        return created;
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> LazyField<T> create(@NonNull Supplier<T> initializer) {
        return new LazyField<>(initializer);
    }
}
