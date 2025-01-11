package com.example.fitnesstracker.domain.profile;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;

import io.reactivex.rxjava3.core.Single;

public interface ProfileRepository {
    @NonNull
    Single<User> uploadAvatar(@NonNull Uri uri);

    @NonNull
    Single<User> editProfile(@NonNull User user);
}
