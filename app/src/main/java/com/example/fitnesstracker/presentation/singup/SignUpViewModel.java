package com.example.fitnesstracker.presentation.singup;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.main.MainPageFragment;
import com.example.fitnesstracker.presentation.singup.action.AgeInput;
import com.example.fitnesstracker.presentation.singup.action.LoginInput;
import com.example.fitnesstracker.presentation.singup.action.NameInput;
import com.example.fitnesstracker.presentation.singup.action.PasswordInput;
import com.example.fitnesstracker.presentation.singup.action.SignUp;
import com.example.fitnesstracker.presentation.singup.action.SignUpScreenAction;
import com.example.fitnesstracker.presentation.singup.state.SignUpScreenState;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SignUpViewModel extends DisposableViewModel<SignUpScreenState, SignUpScreenAction> {
    private final AuthRepository repository;
    private final Router router;

    @Inject
    public SignUpViewModel(AuthRepository authRepository, Router appRouter) {
        super(SignUpScreenState.empty);
        repository = authRepository;
        router = appRouter;
    }

    @Override
    public void onAction(@NonNull SignUpScreenAction action) {
        if (action instanceof AgeInput ageInput) {
            onAgeInput(ageInput.age());
        } else if (action instanceof LoginInput loginInput) {
            onLoginInput(loginInput.login());
        } else if (action instanceof NameInput nameInput) {
            onNameInput(nameInput.name());
        } else if (action instanceof PasswordInput passwordInput) {
            onPasswordInput(passwordInput.password());
        } else if (action instanceof SignUp) {
            onSignUpClick();
        }
    }

    private void onAgeInput(String age) {
        updateState(state ->
                new SignUpScreenState(
                        state.login(),
                        state.password(),
                        age,
                        state.name(),
                        false
                )
        );
    }

    private void onLoginInput(String login) {
        updateState(state ->
                new SignUpScreenState(
                        login,
                        state.password(),
                        state.age(),
                        state.name(),
                        false
                )
        );
    }

    private void onNameInput(String name) {
        updateState(state ->
                new SignUpScreenState(
                        state.login(),
                        state.password(),
                        state.age(),
                        name,
                        false
                )
        );
    }

    private void onPasswordInput(String password) {
        updateState(state ->
                new SignUpScreenState(
                        state.login(),
                        password,
                        state.age(),
                        state.name(),
                        false
                )
        );
    }

    private void onSignUpClick() {
        final var state = stateSubject.getValue();
        Integer age = null;

        try {
            age = Integer.parseInt(state.age());
        } catch (NumberFormatException | NullPointerException ignored) {

        }

        final var signUpResult = repository
                .signUp(state.login(), state.password(), age, state.name())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::navigateToMainPage)
                .doOnError(ignored -> processSignUpError());

        onSubscribe(() -> signUpResult);
    }

    private void navigateToMainPage(User user) {
        router.navigateTo(MainPageFragment.getScreen(user));
    }

    private void processSignUpError() {
        updateState(state ->
                new SignUpScreenState(
                        state.login(),
                        state.password(),
                        state.age(),
                        state.name(),
                        true
                )
        );
    }
}
