package com.example.fitnesstracker.presentation.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

// TODO state/action
@HiltViewModel
public class MainViewModel extends DisposableViewModel<Object, Object> {
    private final WorkoutRepository repository;

    @Inject
    public MainViewModel(WorkoutRepository repository) {
        super(new Object());
        this.repository = repository;
        observePagingSource();
    }

    private void observePagingSource() {
        // TODO subscribe with flowable
        repository
                .observeWorkouts()
                .doOnNext(workoutPagingData -> {})
                .subscribe((wp) -> {});
    }

    @Override
    public void onAction(@NonNull Object action) {

    }
}
