package com.example.fitnesstracker.data.storage;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Flowable;

public interface ObservableStorage<T> extends Storage<T> {
    @NonNull
    Flowable<T> observe();
}
