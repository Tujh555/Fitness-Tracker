package com.example.fitnesstracker.presentation.basic.fragment.disposable;

import io.reactivex.rxjava3.core.Single;

@FunctionalInterface
public interface SingleFactory {
    Single<?> create();
}
