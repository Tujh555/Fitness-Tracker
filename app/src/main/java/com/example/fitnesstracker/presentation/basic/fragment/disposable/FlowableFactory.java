package com.example.fitnesstracker.presentation.basic.fragment.disposable;

import io.reactivex.rxjava3.core.Flowable;

@FunctionalInterface
public interface FlowableFactory {
    Flowable<?> create();
}
