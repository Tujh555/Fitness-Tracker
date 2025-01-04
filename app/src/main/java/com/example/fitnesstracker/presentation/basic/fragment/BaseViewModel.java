package com.example.fitnesstracker.presentation.basic.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import io.reactivex.rxjava3.core.Flowable;

public interface BaseViewModel<S, A> {
    @NonNull
    Flowable<S> observeState();

    void onAction(@NonNull A action);

    void setFragmentManager(@Nullable FragmentManager manager);
}
