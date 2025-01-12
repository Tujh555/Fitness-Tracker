package com.example.fitnesstracker.domain.profile;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface ProfileRepository {
    @NonNull
    Flowable<User> observe();

    @NonNull
    Completable uploadAvatar(@NonNull Uri uri);

    @NonNull
    Single<User> editProfile(
            @NonNull String name,
            @NonNull String login,
            @Nullable Integer age,
            @NonNull String target
    );
}
