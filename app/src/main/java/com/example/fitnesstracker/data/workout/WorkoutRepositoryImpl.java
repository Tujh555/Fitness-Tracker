package com.example.fitnesstracker.data.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

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
import com.example.fitnesstracker.data.rest.StreamRequestBody;
import com.example.fitnesstracker.data.rest.dto.ExerciseDto;
import com.example.fitnesstracker.data.rest.workout.WorkoutApi;
import com.example.fitnesstracker.domain.workout.WorkoutRepository;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;

@OptIn(markerClass = ExperimentalPagingApi.class)
public class WorkoutRepositoryImpl implements WorkoutRepository {
    private final @NonNull WorkoutDao workoutDao;
    private final @NonNull WorkoutsMediator workoutsMediator;
    private final @NonNull Context context;
    private final @NonNull WorkoutApi workoutApi;

    @Inject
    public WorkoutRepositoryImpl(
            @NonNull WorkoutDao workoutDao,
            @NonNull WorkoutsMediator workoutsMediator,
            @NonNull @ApplicationContext Context context,
            @NonNull WorkoutApi workoutApi
    ) {
        this.workoutDao = workoutDao;
        this.workoutsMediator = workoutsMediator;
        this.context = context;
        this.workoutApi = workoutApi;
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

    /** @noinspection ResultOfMethodCallIgnored*/
    @SuppressLint("CheckResult")
    @Override
    public Single<Exercise> putExerciseDescription(@NonNull String id, @NonNull Uri uri) {
        final Single<MultipartBody.Part> createdBody = Single.create(emitter -> {
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

        return createdBody
                .flatMap(body -> workoutApi.putExerciseDescription(id, body))
                .doOnSuccess(exercise -> workoutDao.putExercisePhoto(exercise.id(), exercise.describingPhoto()))
                .subscribeOn(Schedulers.io())
                .map(ExerciseDto::toDomain);
    }
}
