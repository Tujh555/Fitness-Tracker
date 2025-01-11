package com.example.fitnesstracker.platform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.profile.ProfileRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public final class UpdateProfilePhotoService extends UploadService {
    private static final @NonNull String uriKey = "photo_uri";
    @Inject
    public ProfileRepository repository;

    @Override
    Single<?> upload(@NonNull Intent intent) {
        return Single.<Uri>create(emitter -> {
            try {
                final Uri uri = intent.getParcelableExtra(uriKey);
                emitter.onSuccess(uri);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).flatMap(repository::uploadAvatar);
    }

    public static void launch(Context context, @NonNull Uri uri) {
        final var intent = new Intent(context, UpdateProfilePhotoService.class)
                .putExtra(uriKey, uri);

        context.startForegroundService(intent);
    }
}
