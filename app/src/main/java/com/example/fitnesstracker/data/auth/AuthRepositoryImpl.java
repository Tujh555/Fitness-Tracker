package com.example.fitnesstracker.data.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.database.WorkoutDatabase;
import com.example.fitnesstracker.data.rest.auth.LogoutRequest;
import com.example.fitnesstracker.data.storage.Storage;
import com.example.fitnesstracker.data.rest.auth.AuthApi;
import com.example.fitnesstracker.data.rest.auth.LoginRequest;
import com.example.fitnesstracker.data.rest.auth.RegisterRequest;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.auth.error.NotExistedUserException;

import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class AuthRepositoryImpl implements AuthRepository {
    private final Storage<UserDto> userStorage;
    private final AuthApi authApi;
    private final Storage<String> tokenStorage;
    private final WorkoutDatabase database;

    @Inject
    public AuthRepositoryImpl(
            Storage<UserDto> storage,
            Storage<String> authTokenStorage,
            AuthApi api,
            WorkoutDatabase database
    ) {
        userStorage = storage;
        authApi = api;
        tokenStorage = authTokenStorage;
        this.database = database;
    }

    @Override
    public Single<User> getExisting() {
        return Single.<User>create(emitter -> {
            final var userDto = userStorage.get();

            if (userDto == null) {
                emitter.onError(new NotExistedUserException());
            } else {
                emitter.onSuccess(userDto.toDomain());
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<User> signIn(@NonNull String login, @NonNull String password) {
        final var loginRequest = new LoginRequest(login, password);

        return authApi
                .login(loginRequest)
                .doOnSuccess(authResponse -> {
                    tokenStorage.save(authResponse.authToken());
                    userStorage.save(authResponse.userDto());
                })
                .map(response -> response.userDto().toDomain())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<User> signUp(
            @NonNull String login,
            @NonNull String password,
            @Nullable Integer age,
            @Nullable String name
    ) {
        final var registerRequest = new RegisterRequest(login, password, age, name);

        return authApi
                .register(registerRequest)
                .doOnSuccess(authResponse -> {
                    tokenStorage.save(authResponse.authToken());
                    userStorage.save(authResponse.userDto());
                })
                .map(response -> response.userDto().toDomain())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable logout() {
        return Single
                .<UserDto>create(emitter -> {
                    final var user = userStorage.get();
                    if (user == null || user.login() == null) {
                        emitter.onError(new IllegalStateException());
                    } else {
                        emitter.onSuccess(user);
                    }
                })
                .flatMapCompletable(user -> {
                    final var logoutRequest = new LogoutRequest(user.id());
                    return authApi.logout(logoutRequest);
                })
                .andThen(database.remoteKeysDao().clear())
                .andThen(Completable.fromAction(() -> database.workoutDao().clear()))
                .doOnComplete(() -> {
                    userStorage.clear();
                    tokenStorage.clear();
                })
                .subscribeOn(Schedulers.io());
    }
}
