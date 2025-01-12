package com.example.fitnesstracker.presentation.exercise.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.exercise.edit.ExerciseEditFragment;
import com.github.terrakok.cicerone.Router;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ExerciseListViewModel extends DisposableViewModel<List<Exercise>, ExerciseListScreenAction> {
    private final @NonNull WorkoutRepository repository;
    private final @NonNull Router router;

    @Inject
    public ExerciseListViewModel(@NonNull WorkoutRepository repository, @NonNull Router router) {
        super(new ArrayList<>(1));
        this.repository = repository;
        this.router = router;
        observeExercises();
    }

    @Override
    public void onAction(@NonNull ExerciseListScreenAction action) {
        if (action instanceof ExerciseListScreenAction.Edit edit) {
            openEditExercise(edit.exercise());
        } else if (action instanceof ExerciseListScreenAction.Create) {
            openEditExercise(null);
        }
    }

    private void openEditExercise(@Nullable Exercise exercise) {
        final var screen = ExerciseEditFragment.getScreen(exercise);
        router.navigateTo(screen);
    }

    private void observeExercises() {
        onSubscribe(() ->
                repository
                        .observeExercises()
                        .doOnNext(list -> updateState((s) -> list))
        );
    }
}
