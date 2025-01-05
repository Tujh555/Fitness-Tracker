package com.example.fitnesstracker.domain.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.User;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<User> getExisting();

    Single<User> signIn(@NonNull String login, @NonNull String password);

    Single<User> signUp(
            @NonNull String login,
            @NonNull String password,
            @Nullable Integer age,
            @Nullable String name
    );
}