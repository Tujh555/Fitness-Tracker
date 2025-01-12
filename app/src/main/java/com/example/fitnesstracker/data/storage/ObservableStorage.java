package com.example.fitnesstracker.data.storage;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.LazyField;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public abstract class ObservableStorage<T> implements Storage<T> {
    private final LazyField<BehaviorSubject<T>> subject = LazyField.create(() -> {
        final var item = get();

        if (item == null) {
            return BehaviorSubject.create();
        }

        return BehaviorSubject.createDefault(item);
    });

    @NonNull
    public final Flowable<T> observe() {
        return subject.get().toFlowable(BackpressureStrategy.LATEST);
    }

    protected final void update(T item) {
        subject.get().onNext(item);
    }
}
