package com.example.fitnesstracker.data.workout;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava3.PagingRx;

import com.example.fitnesstracker.data.database.WorkoutDao;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;
import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@OptIn(markerClass = ExperimentalPagingApi.class)
public class WorkoutRepositoryImpl implements WorkoutRepository {
    private final @NonNull WorkoutDao workoutDao;
    private final @NonNull WorkoutsMediator workoutsMediator;

    @Inject
    public WorkoutRepositoryImpl(
            @NonNull WorkoutDao workoutDao,
            @NonNull WorkoutsMediator workoutsMediator
    ) {
        this.workoutDao = workoutDao;
        this.workoutsMediator = workoutsMediator;
    }

    @NonNull
    @Override
    public Flowable<PagingData<Workout>> observeWorkouts() {
        final var config = new PagingConfig(20, 5, true, 20);
        final var pager = new Pager<>(config, 1, workoutsMediator, workoutDao::selectAll);
        final var executor = new Executor() {
            @Override
            public void execute(Runnable runnable) {
                Schedulers.io().scheduleDirect(runnable);
            }
        };
        return PagingRx
                .getFlowable(pager)
                .map(pagingData ->
                        PagingDataTransforms
                                .map(pagingData, executor, WorkoutWithExercises::toDomain)
                );
    }
}
