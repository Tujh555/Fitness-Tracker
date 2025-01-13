package com.example.fitnesstracker.data.workout;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava3.PagingRx;

import com.example.fitnesstracker.data.database.WorkoutDao;
import com.example.fitnesstracker.data.database.entities.ApproachEntity;
import com.example.fitnesstracker.data.database.entities.ExerciseEntity;
import com.example.fitnesstracker.data.rest.StreamRequestBody;
import com.example.fitnesstracker.data.rest.dto.ApproachDto;
import com.example.fitnesstracker.data.rest.dto.ExerciseDto;
import com.example.fitnesstracker.data.rest.dto.SummaryDto;
import com.example.fitnesstracker.data.rest.dto.WorkoutDto;
import com.example.fitnesstracker.data.rest.workout.WorkoutApi;
import com.example.fitnesstracker.data.storage.ObservableStorage;
import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.WorkoutSummary;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@OptIn(markerClass = ExperimentalPagingApi.class)
public class WorkoutRepositoryImpl implements WorkoutRepository {
    private final @NonNull WorkoutDao workoutDao;
    private final @NonNull WorkoutsMediator workoutsMediator;
    private final @NonNull Context context;
    private final @NonNull WorkoutApi workoutApi;
    private final @NonNull ObservableStorage<List<SummaryDto>> summaryStorage;

    @Inject
    public WorkoutRepositoryImpl(
            @NonNull WorkoutDao workoutDao,
            @NonNull WorkoutsMediator workoutsMediator,
            @NonNull @ApplicationContext Context context,
            @NonNull WorkoutApi workoutApi,
            @NonNull ObservableStorage<List<SummaryDto>> summaryStorage
    ) {
        this.workoutDao = workoutDao;
        this.workoutsMediator = workoutsMediator;
        this.context = context;
        this.workoutApi = workoutApi;
        this.summaryStorage = summaryStorage;
    }

    @NonNull
    @Override
    public Flowable<PagingData<Workout>> observeWorkouts() {
        final var config = new PagingConfig(20, 5, true, 20);
        final var pager = new Pager<>(config, 1, workoutsMediator, workoutDao::selectWorkouts);
        final var executor = new Executor() {
            @Override
            public void execute(Runnable runnable) {
                Schedulers.io().scheduleDirect(runnable);
            }
        };
        final var workouts = PagingRx.getFlowable(pager);
        final var approaches = workoutDao.selectApproaches();

        return Flowable.combineLatest(workouts, approaches, (workoutsPaging, approachList) ->
                PagingDataTransforms.map(workoutsPaging, executor, (workout) -> {
                    final var approachMap = ApproachEntity.associate(approachList);
                    return workout.toDomain(approachMap);
                })
        );
    }

    @Override
    @NonNull
    public Completable putExerciseDescription(@NonNull String id, @Nullable Uri uri, @NonNull String title) {
        final Single<ExerciseDto> response;

        if (uri != null) {
            response = createMultipart(uri).flatMap(body -> {
                final var titleBody = RequestBody.create(title, MediaType.get("text/plain"));
                return workoutApi.putExercise(id, body, titleBody);
            });
        } else {
            final var titleBody = RequestBody.create(title, MediaType.get("text/plain"));
            response = workoutApi.putExercise(id, null, titleBody);
        }

        return response
                .flatMapCompletable(exercise ->
                        workoutDao.putExercisePhoto(exercise.id(), exercise.describingPhoto())
                )
                .subscribeOn(Schedulers.io());
    }

    private Single<MultipartBody.Part> createMultipart(@NonNull Uri uri) {
        return Single.create(emitter -> {
            try {
                final var stream = context
                        .getContentResolver()
                        .openInputStream(uri);
                final var body = new StreamRequestBody(stream);
                final var multipart = MultipartBody.Part.createFormData("photo", "", body);
                emitter.onSuccess(multipart);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @NonNull
    @Override
    public Completable createExercise(@Nullable Uri uri, @NonNull String title) {
        final Single<ExerciseDto> response;

        if (uri != null) {
            response = createMultipart(uri).flatMap(body -> {
                final var titleBody = RequestBody.create(title, MediaType.get("text/plain"));
                return workoutApi.putExercise(
                        UUID.randomUUID().toString(),
                        body,
                        titleBody
                );
            });
        } else {
            final var titleBody = RequestBody.create(title, MediaType.get("text/plain"));
            response = workoutApi.putExercise(
                    UUID.randomUUID().toString(),
                    null,
                    titleBody
            );
        }

        return response
                .flatMapCompletable(exercise -> {
                    final var exercises = new ArrayList<ExerciseEntity>();
                    exercises.add(exercise.toDb());
                    return workoutDao.insertExercises(exercises);
                })
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable createWorkout(
            @NonNull String title,
            @NonNull List<String> exerciseIds,
            @NonNull Map<String, List<Pair<Integer, Integer>>> approaches
    ) {
        return workoutDao
                .selectExercises(exerciseIds)
                .flatMapCompletable(exerciseEntities -> {
                    final var workoutId = UUID.randomUUID().toString();
                    final var date = Instant.now().toString();

                    final var exercises = exerciseEntities
                            .stream()
                            .map(exercise -> {
                                final var approachDtos = approaches
                                        .getOrDefault(exercise.exerciseId, new ArrayList<>(0))
                                        .stream()
                                        .map(pair ->
                                                new ApproachDto(
                                                        UUID.randomUUID().toString(),
                                                        workoutId,
                                                        exercise.exerciseId,
                                                        pair.first(),
                                                        pair.second()
                                                )
                                        )
                                        .collect(Collectors.toList());

                                return new ExerciseDto(
                                        exercise.exerciseId,
                                        exercise.title,
                                        exercise.describingPhoto,
                                        approachDtos
                                );
                            })
                            .collect(Collectors.toList());

                    final var workout = new WorkoutDto(
                            workoutId,
                            title,
                            date,
                            exercises
                    );

                    return workoutApi.createWorkout(workout);
                });
    }

    @NonNull
    @Override
    public Flowable<List<WorkoutSummary>> observeSummary() {
        return summaryStorage
                .observe()
                .doOnSubscribe((s) ->
                        workoutApi
                                .getSummary()
                                .subscribeOn(Schedulers.io())
                                .subscribe(summaryStorage::save, e -> {
                                })
                )
                .map(summaries ->
                        summaries
                                .stream()
                                .map(SummaryDto::toDomain)
                                .collect(Collectors.toList())
                )
                .subscribeOn(Schedulers.io())
                .distinct();
    }

    @NonNull
    @Override
    public Flowable<List<Exercise>> observeExercises() {
        return workoutDao
                .selectExercises()
                .doOnSubscribe(s ->
                        workoutApi
                                .getExercises()
                                .flatMapCompletable(exercises -> {
                                    final var entities = exercises
                                            .stream()
                                            .map(ExerciseDto::toDb)
                                            .collect(Collectors.toList());
                                    return workoutDao.insertExercises(entities);
                                })
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                }, e -> {
                                })
                )
                .map(exercises ->
                        exercises
                                .stream()
                                .map(ExerciseEntity::toDomain)
                                .collect(Collectors.toList())
                )
                .subscribeOn(Schedulers.io());
    }
}
