package com.example.fitnesstracker.presentation.workout.create;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.main.MainPageFragment;
import com.example.fitnesstracker.presentation.workout.create.action.WorkoutAppendScreenAction;
import com.example.fitnesstracker.presentation.workout.create.state.WorkoutAppendScreenState;
import com.github.terrakok.cicerone.Router;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class WorkoutAppendViewModel extends DisposableViewModel<WorkoutAppendScreenState, WorkoutAppendScreenAction> {
    private final @NonNull WorkoutRepository repository;
    private final @NonNull Context context;
    private final @NonNull Router router;

    @Inject
    public WorkoutAppendViewModel(
            @NonNull WorkoutRepository repository,
            @NonNull @ApplicationContext Context context,
            @NonNull Router router
    ) {
        super(WorkoutAppendScreenState.empty);
        this.repository = repository;
        this.context = context;
        this.router = router;
        observeExercises();
    }

    @Override
    public void onAction(@NonNull WorkoutAppendScreenAction action) {
        if (action instanceof WorkoutAppendScreenAction.Clear) {
            clear();
        } else if (action instanceof WorkoutAppendScreenAction.Save) {
            save();
        } else if (action instanceof WorkoutAppendScreenAction.Title title) {
            titleInput(title.text());
        } else if (action instanceof WorkoutAppendScreenAction.AppendExercise appendExercise) {
            appendExercise(appendExercise.exercise(), appendExercise.repetitions(), appendExercise.weight());
        } else if (action instanceof WorkoutAppendScreenAction.ApplyExisting existing) {
            applyExisting(existing.workout());
        } else if (action instanceof WorkoutAppendScreenAction.SetDate setDate) {
            setDate(setDate.time());
        }
    }

    private void setDate(@NonNull Instant date) {
        updateState(state ->
                new WorkoutAppendScreenState(
                        state.existingId(),
                        state.title(),
                        date,
                        state.availableExercises(),
                        state.selectedExercises()
                )
        );
    }

    private void titleInput(@NonNull String text) {
        updateState(state ->
                new WorkoutAppendScreenState(
                        state.existingId(),
                        text,
                        state.date(),
                        state.availableExercises(),
                        state.selectedExercises()
                )
        );
    }

    private void applyExisting(@NonNull Workout workout) {
        final var exercisePairs = workout
                .exercises()
                .stream()
                .map(exercise -> {
                    final var approaches = workout
                            .approaches()
                            .getOrDefault(exercise.id(), new ArrayList<>())
                            .stream()
                            .map(approach -> new Pair<>(approach.repetitions(), approach.weight()))
                            .collect(Collectors.toList());

                    return new Pair<>(exercise, approaches);
                })
                .collect(Collectors.toList());

        updateState(state ->
                new WorkoutAppendScreenState(
                        workout.id(),
                        workout.title(),
                        workout.date(),
                        state.availableExercises(),
                        exercisePairs
                )
        );
    }

    private void appendExercise(@NonNull Exercise exercise, int repetitions, int weight) {
        stateSubject
                .getValue()
                .selectedExercises()
                .stream()
                .filter(selectedPair -> selectedPair.first().id().equals(exercise.id()))
                .findFirst()
                .ifPresentOrElse(
                        (selectedPair) -> {
                            final var approach = new Pair<>(repetitions, weight);
                            final List<Pair<Integer, Integer>> approaches = new ArrayList<>(selectedPair.second());
                            approaches.add(approach);
                            List<Pair<Exercise, List<Pair<Integer, Integer>>>> upd = stateSubject
                                    .getValue()
                                    .selectedExercises()
                                    .stream()
                                    .map(p -> {
                                        if (p.first().id().equals(selectedPair.first().id())) {
                                            return new Pair<>(p.first(), approaches);
                                        }

                                        return selectedPair;
                                    })
                                    .collect(Collectors.toList());

                            updateState(state ->
                                    new WorkoutAppendScreenState(
                                            state.existingId(),
                                            state.title(),
                                            state.date(),
                                            state.availableExercises(),
                                            upd
                                    )
                            );
                        },
                        () -> {
                            final var approach = new Pair<>(repetitions, weight);
                            final var list = new ArrayList<Pair<Integer, Integer>>();
                            list.add(approach);
                            final var ex = new ArrayList<>(stateSubject.getValue().selectedExercises());
                            ex.add(new Pair<>(exercise, list));
                            updateState(state ->
                                    new WorkoutAppendScreenState(
                                            state.existingId(),
                                            state.title(),
                                            state.date(),
                                            state.availableExercises(),
                                            ex
                                    )
                            );
                        }
                );
    }

    private void clear() {
        updateState(s ->
                new WorkoutAppendScreenState(
                        null,
                        "",
                        null,
                        s.availableExercises(),
                        new ArrayList<>()
                )
        );
    }

    private void save() {
        final var state = stateSubject.getValue();
        final var exercises = state
                .selectedExercises()
                .stream()
                .map(Pair::first)
                .collect(Collectors.toList());
        final var approaches = new HashMap<String, List<Pair<Integer, Integer>>>();

        state.selectedExercises().forEach(pair -> {
            final var exercise = pair.first();
            final var list = approaches.get(exercise.id());

            if (list != null) {
                list.addAll(pair.second());
            } else {
                approaches.put(exercise.id(), new ArrayList<>(pair.second()));
            }
        });
        final var date = state.date() == null ? Instant.now() : state.date();
        Completable completable;

        if (state.existingId() != null) {
            completable = repository
                    .editWorkout(state.existingId(), state.title(), date, exercises, approaches);
        } else {
            completable = repository.createWorkout(state.title(), date, exercises, approaches);
        }

        completable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (state.existingId() != null) {
                                Toast
                                        .makeText(context, "Тренировка отредактирована", Toast.LENGTH_SHORT)
                                        .show();
                                router.exit();
                            } else {
                                Toast
                                        .makeText(context, "Тренировка добавлена", Toast.LENGTH_SHORT)
                                        .show();
                                clear();
                            }
                        },
                        e -> {},
                        disposables
                );
    }

    private void observeExercises() {
        onSubscribe(() ->
                repository
                        .observeExercises()
                        .doOnNext(this::appendAvailableExercises)
        );
    }

    private void appendAvailableExercises(List<Exercise> exercises) {
        updateState(state ->
                new WorkoutAppendScreenState(
                        state.existingId(),
                        state.title(),
                        state.date(),
                        exercises,
                        state.selectedExercises()
                )
        );
    }

    public List<Exercise> availableExercises() {
        return stateSubject.getValue().availableExercises();
    }
}
