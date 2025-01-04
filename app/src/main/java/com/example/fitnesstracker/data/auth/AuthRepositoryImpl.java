package com.example.fitnesstracker.data.auth;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.auth.error.InvalidCredentialsException;
import com.example.fitnesstracker.domain.auth.error.NotExistedUserException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class AuthRepositoryImpl implements AuthRepository {

    @Inject
    public AuthRepositoryImpl() {}

    @Override
    public Single<User> getExisting() {
        return Single.error(NotExistedUserException::new);
    }

    @Override
    public Single<User> signIn(@NonNull String login, @NonNull String password) {
        return Single.error(NotExistedUserException::new);
    }

    @Override
    public Single<User> signUp(@NonNull String login, @NonNull String password) {
        return Single.error(InvalidCredentialsException::new);
    }
}
