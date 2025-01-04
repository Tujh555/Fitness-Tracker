package com.example.fitnesstracker.presentation.auth;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.auth.error.InvalidCredentialsException;
import com.example.fitnesstracker.domain.auth.error.NotExistedUserException;
import com.example.fitnesstracker.presentation.auth.action.AuthScreenAction;
import com.example.fitnesstracker.presentation.auth.action.LoginInput;
import com.example.fitnesstracker.presentation.auth.action.PasswordInput;
import com.example.fitnesstracker.presentation.auth.action.SignIn;
import com.example.fitnesstracker.presentation.auth.action.SignUp;
import com.example.fitnesstracker.presentation.auth.error.AuthError;
import com.example.fitnesstracker.presentation.auth.error.InvalidCredentials;
import com.example.fitnesstracker.presentation.auth.error.NotExistedUser;
import com.example.fitnesstracker.presentation.auth.error.Unknown;
import com.example.fitnesstracker.presentation.auth.state.AuthScreenState;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.main.MainPageFragment;
import com.example.fitnesstracker.presentation.singup.SingUpFragment;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AuthViewModel extends DisposableViewModel<AuthScreenState, AuthScreenAction> {
    private final AuthRepository repository;

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        super(new AuthScreenState("", "", null));
        repository = authRepository;
        checkAlreadyAuthorized();
    }

    @Override
    public void onAction(@NonNull AuthScreenAction action) {
        if (action instanceof LoginInput loginInput) {
            loginInput(loginInput.login());
        } else if (action instanceof PasswordInput passwordInput) {
            passwordInput(passwordInput.password());
        } else if (action instanceof SignIn) {
            singIn();
        } else if (action instanceof SignUp) {
            signUp();
        }
    }

    private void loginInput(String login) {
        updateState((state) -> new AuthScreenState(login, state.password(), state.error()));
    }

    private void passwordInput(String password) {
        updateState((state) -> new AuthScreenState(state.login(), password, state.error()));
    }

    private void singIn() {
        final var singInResult = withState((state) -> repository.signIn(state.login(), state.password()));
        if (singInResult == null) {
            return;
        }

        onSubscribe(() ->
                singInResult
                        .doOnSuccess(this::navigateToMainPage)
                        .doOnError(this::processSignInError)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
        );
    }

    private void navigateToMainPage(@NonNull User user) {
        navigate((fragmentManager) ->
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, new MainPageFragment())
                        .commit()
        );
    }

    private void processSignInError(Throwable throwable) {
        final AuthError error;

        if (throwable instanceof InvalidCredentialsException) {
            error = new InvalidCredentials();
        } else if (throwable instanceof NotExistedUserException) {
            error = new NotExistedUser();
        } else {
            error = new Unknown();
        }

        updateState((state) -> new AuthScreenState("", "", error));
    }

    private void signUp() {
        navigate((fragmentManager) ->
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, new SingUpFragment())
                        .commit()
        );
    }

    private void checkAlreadyAuthorized() {
        onSubscribe(() ->
                repository
                        .getExisting()
                        .doOnSuccess(this::navigateToMainPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
        );
    }
}
