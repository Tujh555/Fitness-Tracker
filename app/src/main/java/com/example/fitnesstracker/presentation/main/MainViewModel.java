package com.example.fitnesstracker.presentation.main;

import androidx.annotation.NonNull;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.main.action.MainPageScreenAction;
import com.example.fitnesstracker.presentation.main.state.MainFragmentState;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends DisposableViewModel<MainFragmentState, MainPageScreenAction> {
    private final WorkoutRepository repository;

    @Inject
    public MainViewModel(WorkoutRepository repository) {
        super(new MainFragmentState(null, new ArrayList<>()));
        this.repository = repository;
        observePagingSource();
    }

    private void observePagingSource() {
        onSubscribe(() -> repository.observeWorkouts().doOnNext(this::updatePagingData));
    }

    @Override
    public void onAction(@NonNull MainPageScreenAction action) {

    }

    private void updatePagingData(PagingData<Workout> workoutPagingData) {
        updateState(state -> new MainFragmentState(workoutPagingData, state.summaries()));
    }
}
