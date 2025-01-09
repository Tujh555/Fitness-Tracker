package com.example.fitnesstracker.data.workout;

import androidx.annotation.NonNull;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxRemoteMediator;

import com.example.fitnesstracker.data.database.WorkoutDao;
import com.example.fitnesstracker.data.database.WorkoutDatabase;
import com.example.fitnesstracker.data.database.entities.ApproachEntity;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;
import com.example.fitnesstracker.data.rest.dto.WorkoutResponse;
import com.example.fitnesstracker.data.rest.workout.WorkoutApi;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@ExperimentalPagingApi
public class WorkoutsMediator extends RxRemoteMediator<Instant, WorkoutWithExercises> {
    private final @NonNull WorkoutApi workoutApi;
    private final @NonNull WorkoutDao workoutDao;
    private final @NonNull WorkoutDatabase database;
    private static final int pageSize = 20;
    private static final Instant emptyDate = Instant.MIN;

    @Inject
    public WorkoutsMediator(
            @NonNull WorkoutApi workoutApi,
            @NonNull WorkoutDao workoutDao,
            @NonNull WorkoutDatabase database
    ) {
        this.workoutApi = workoutApi;
        this.workoutDao = workoutDao;
        this.database = database;
    }


    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(
            @NonNull LoadType loadType,
            @NonNull PagingState<Instant, WorkoutWithExercises> pagingState
    ) {
        return Single
                .just(loadType)
                .subscribeOn(Schedulers.io())
                .map(type -> keyForType(loadType, pagingState))
                .flatMap(time -> fetch(time, loadType))
                .onErrorReturn(MediatorResult.Error::new);
    }

    private @NonNull Instant keyForType(
            @NonNull LoadType type,
            @NonNull PagingState<Instant, WorkoutWithExercises> pagingState
    ) {
        switch (type) {
            case REFRESH -> {
                return closestKey(pagingState);
            }
            case PREPEND -> {
                return firstItemKey(pagingState);
            }
            case APPEND -> {
                return lastItemKey(pagingState);
            }
        }

        return emptyDate;
    }

    private @NonNull Single<MediatorResult> fetch(@NonNull Instant time, @NonNull LoadType type) {
        if (time == emptyDate) {
            return Single.just(new MediatorResult.Success(true));
        }

        final Single<List<WorkoutResponse>> single;

        switch (type) {
            case REFRESH, PREPEND ->
                    single = workoutApi.getWorkoutsAfter(time.toString(), pageSize);

            case APPEND ->
                    single = workoutApi.getWorkoutsBefore(time.toString(), pageSize);

            default -> single = Single.just(new ArrayList<>());
        }

        return single.map(workoutResponses -> {
            final var workouts = workoutResponses
                    .stream()
                    .map(WorkoutResponse::toDomain)
                    .collect(Collectors.toList());

            insertToDb(workouts, type);

            return new MediatorResult.Success(workouts.size() < pageSize);
        });
    }

    private void insertToDb(@NonNull List<Workout> workouts, @NonNull LoadType loadType) {
        database.runInTransaction(() -> {
            final Completable clearCompletable;

            if (loadType.equals(LoadType.REFRESH)) {
                clearCompletable = workoutDao.clear();
            } else {
                clearCompletable = Completable.complete();
            }

            final var workoutEntities = new ArrayList<WorkoutEntity>(workouts.size());
            final var exerciseEntities = new ArrayList<ExerciseEntity>(workouts.size());
            final var approachEntities = new ArrayList<ApproachEntity>(workouts.size());

            workouts.forEach(workout -> {
                workoutEntities.add(WorkoutEntity.toDb(workout));

                workout.exercises().forEach(exercise -> {
                    exerciseEntities.add(ExerciseEntity.toDb(workout.id(), exercise));

                    exercise.approaches().forEach(approach ->
                            approachEntities.add(ApproachEntity.toDb(exercise.id(), approach))
                    );
                });
            });

            clearCompletable
                    .andThen(workoutDao.insertWorkouts(workoutEntities))
                    .andThen(workoutDao.insertExercises(exerciseEntities))
                    .andThen(workoutDao.insertApproach(approachEntities))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .blockingAwait();
        });
    }

    private @NonNull Instant firstItemKey(
            @NonNull PagingState<Instant, WorkoutWithExercises> pagingState
    ) {
        final var firstPage = pagingState
                .getPages()
                .stream()
                .filter(data -> !data.getData().isEmpty())
                .findFirst();

        return firstPage
                .map(workouts -> workouts.getData().get(0).workout.date)
                .orElse(emptyDate);
    }

    private @NonNull Instant lastItemKey(
            @NonNull PagingState<Instant, WorkoutWithExercises> pagingState
    ) {
        for (int i = pagingState.getPages().size() - 1; i >= 0; i--) {
            final var page = pagingState.getPages().get(i);
            if (!page.getData().isEmpty()) {
                return page.getData().get(page.getData().size() - 1).workout.date;
            }
        }

        return emptyDate;
    }

    private @NonNull Instant closestKey(
            @NonNull PagingState<Instant, WorkoutWithExercises> pagingState
    ) {
        final var anchorPosition = pagingState.getAnchorPosition();

        if (anchorPosition == null) {
            return emptyDate;
        }
        final var closest = pagingState.closestItemToPosition(anchorPosition);
        if (closest == null) {
            return emptyDate;
        }

        return closest.workout.date;
    }
}
