package com.example.fitnesstracker.platform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.workout.WorkoutRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public class PutExerciseService extends UploadService {
    private static final @NonNull String uriKey = "uri";
    private static final @NonNull String titleKey = "title";
    private static final @NonNull String idKey = "id";

    @Inject
    WorkoutRepository repository;

    @Override
    Completable doWork(@NonNull Intent intent) {
        final var existingId = intent.getStringExtra(idKey);
        final var title = intent.getStringExtra(titleKey);
        final Uri uri = intent.getParcelableExtra(uriKey);
        final var targetTitle = title != null ? title : "Не заполнено";

        if (existingId == null) {
            return repository.createExercise(uri, targetTitle);
        }

        return repository.putExerciseDescription(existingId, uri, targetTitle);
    }

    public static void create(Context context, @Nullable Uri uri, @NonNull String title) {
        final var intent = new Intent(context, PutExerciseService.class).putExtra(titleKey, title);

        if (uri != null) {
            intent.putExtra(uriKey, uri);
        }

        context.startForegroundService(intent);
    }

    public static void edit(Context context, @NonNull String id, @Nullable Uri uri, @NonNull String title) {
        final var intent = new Intent(context, PutExerciseService.class)
                .putExtra(titleKey, title)
                .putExtra(idKey, id);

        if (uri != null) {
            intent.putExtra(uriKey, uri);
        }

        context.startForegroundService(intent);
    }
}
