package com.example.fitnesstracker.presentation.main;

import androidx.annotation.NonNull;
import androidx.paging.PagingData;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.main.action.MainPageScreenAction;
import com.example.fitnesstracker.presentation.main.state.MainFragmentState;
import com.example.fitnesstracker.presentation.workout.create.WorkoutAppendFragment;
import com.github.terrakok.cicerone.Router;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends DisposableViewModel<MainFragmentState, MainPageScreenAction> {
    private final WorkoutRepository repository;
    private final @NonNull Router router;

    @Inject
    public MainViewModel(WorkoutRepository repository, @NonNull Router router) {
        super(new MainFragmentState(null, new ArrayList<>()));
        this.repository = repository;
        this.router = router;
        observePagingSource();
    }

    private void observePagingSource() {
        onSubscribe(() -> repository.observeWorkouts().doOnNext(this::updatePagingData));
    }

    @Override
    public void onAction(@NonNull MainPageScreenAction action) {
        if (action instanceof MainPageScreenAction.Edit edit) {
            edit(edit.workout());
        }
    }

    private void edit(@NonNull Workout workout) {
        final var screen = WorkoutAppendFragment.getScreen(workout);
        router.navigateTo(screen);
    }

    private void updatePagingData(PagingData<Workout> workoutPagingData) {
        updateState(state -> new MainFragmentState(workoutPagingData, state.summaries()));
    }
}
