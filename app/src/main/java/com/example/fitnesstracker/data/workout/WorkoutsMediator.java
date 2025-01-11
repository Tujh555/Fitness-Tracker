package com.example.fitnesstracker.data.workout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxRemoteMediator;

import com.example.fitnesstracker.data.database.WorkoutDatabase;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.database.entities.RemoteKeys;
import com.example.fitnesstracker.data.database.entities.WorkoutCrossRef;
import com.example.fitnesstracker.data.database.entities.WorkoutEntity;
import com.example.fitnesstracker.data.database.entities.WorkoutWithExercises;
import com.example.fitnesstracker.data.rest.dto.WorkoutDto;
import com.example.fitnesstracker.data.rest.workout.WorkoutApi;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@ExperimentalPagingApi
public class WorkoutsMediator extends RxRemoteMediator<Integer, WorkoutWithExercises> {
    private final @NonNull WorkoutApi workoutApi;
    private final @NonNull WorkoutDatabase database;
    private static final int pageSize = 20;
    private static final int invalidPage = -1;

    @Inject
    public WorkoutsMediator(
            @NonNull WorkoutApi workoutApi,
            @NonNull WorkoutDatabase database
    ) {
        this.workoutApi = workoutApi;
        this.database = database;
    }


    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(
            @NonNull LoadType loadType,
            @NonNull PagingState<Integer, WorkoutWithExercises> pagingState
    ) {
        return Single
                .just(loadType)
                .subscribeOn(Schedulers.io())
                .map(type -> pageFor(loadType, pagingState))
                .flatMap(page -> fetch(page, loadType))
                .onErrorReturn(MediatorResult.Error::new);
    }

    private @NonNull Integer pageFor(
            @NonNull LoadType type,
            @NonNull PagingState<Integer, WorkoutWithExercises> pagingState
    ) {
        switch (type) {
            case REFRESH -> {
                final var keys = closestKey(pagingState);
                if (keys == null || keys.next == null) {
                    return 1;
                }
                return keys.next - 1;
            }
            case PREPEND -> {
                final var keys = firstItemKey(pagingState);

                if (keys == null || keys.prev == null) {
                    throw new IllegalStateException();
                }

                return keys.prev;
            }
            case APPEND -> {
                final var keys = lastItemKey(pagingState);

                if (keys == null || keys.next == null) {
                    throw new IllegalStateException();
                }

                return keys.next;
            }
        }

        return invalidPage;
    }

    private @NonNull Single<MediatorResult> fetch(@NonNull Integer page, @NonNull LoadType type) {
        if (page == invalidPage) {
            return Single.just(new MediatorResult.Success(true));
        }

        final var single = workoutApi.getWorkouts(page, pageSize);

        return single.map(workoutResponse -> {
            final var workouts = workoutResponse
                    .workouts()
                    .stream()
                    .map(WorkoutDto::toDomain)
                    .collect(Collectors.toList());

            final boolean lastPage = Objects.equals(
                    workoutResponse.page(),
                    workoutResponse.totalPages()
            );

            insertToDb(page, workouts, type, lastPage);

            return new MediatorResult.Success(lastPage);
        });
    }

    private void insertToDb(
            int page,
            @NonNull List<Workout> workouts,
            @NonNull LoadType loadType,
            boolean lastPage
    ) {
        final var workoutDao = database.workoutDao();
        final var keysDao = database.remoteKeysDao();
        database.runInTransaction(() -> {
            final Completable clearCompletable;

            if (loadType.equals(LoadType.REFRESH)) {
                clearCompletable = workoutDao.clear().andThen(keysDao.clear());
            } else {
                clearCompletable = Completable.complete();
            }

            final var workoutEntities = new ArrayList<WorkoutEntity>(workouts.size());
            final var exerciseEntities = new ArrayList<ExerciseEntity>(workouts.size());
            final var crossRefs = new ArrayList<WorkoutCrossRef>(workouts.size());

            workouts.forEach(workout -> {
                workoutEntities.add(WorkoutEntity.toDb(workout));

                workout.exercises().forEach(exercise -> {
                    exerciseEntities.add(ExerciseEntity.toDb(exercise));
                    crossRefs.add(new WorkoutCrossRef(workout.id(), exercise.id()));
                });
            });

            Integer prevKey;
            Integer nextKey;

            if (page != 1) {
                prevKey = page - 1;
            } else {
                prevKey = null;
            }

            if (!lastPage) {
                nextKey = page + 1;
            } else {
                nextKey = null;
            }

            final var keysInsertCompletable = Completable.fromAction(() -> {
                final var keys = workouts
                        .stream()
                        .map(workout -> new RemoteKeys(workout.id(), prevKey, nextKey))
                        .collect(Collectors.toList());

                keysDao.insert(keys);
            });

            clearCompletable
                    .andThen(keysInsertCompletable)
                    .andThen(workoutDao.insertWorkouts(workoutEntities))
                    .andThen(workoutDao.insertExercises(exerciseEntities))
                    .andThen(workoutDao.insertCrossRef(crossRefs))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .blockingAwait();
        });
    }

    private @Nullable RemoteKeys firstItemKey(
            @NonNull PagingState<Integer, WorkoutWithExercises> pagingState
    ) {
        final var firstPage = pagingState
                .getPages()
                .stream()
                .filter(data -> !data.getData().isEmpty())
                .findFirst();

        return firstPage
                .map(workouts -> {
                    final var id = workouts.getData().get(0).workout.id;
                    return database.remoteKeysDao().resolve(id);
                })
                .orElse(null);
    }

    private @Nullable RemoteKeys lastItemKey(
            @NonNull PagingState<Integer, WorkoutWithExercises> pagingState
    ) {
        for (int i = pagingState.getPages().size() - 1; i >= 0; i--) {
            final var page = pagingState.getPages().get(i);
            if (!page.getData().isEmpty()) {
                final var workoutId = page
                        .getData()
                        .get(page.getData().size() - 1)
                        .workout.id;

                return database.remoteKeysDao().resolve(workoutId);
            }
        }

        return null;
    }

    private @Nullable RemoteKeys closestKey(
            @NonNull PagingState<Integer, WorkoutWithExercises> pagingState
    ) {
        final var anchorPosition = pagingState.getAnchorPosition();

        if (anchorPosition == null) {
            return null;
        }
        final var closest = pagingState.closestItemToPosition(anchorPosition);
        if (closest == null) {
            return null;
        }


        return database.remoteKeysDao().resolve(closest.workout.id);
    }
}
