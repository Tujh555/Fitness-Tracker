package com.example.fitnesstracker.presentation.auth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.databinding.FragmentAuthBinding;
import com.example.fitnesstracker.presentation.SimpleTextWatcher;
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
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthFragment extends BaseFragment<AuthScreenState, AuthScreenAction, FragmentAuthBinding, AuthViewModel> {

    @Override
    protected void onStateChanged(@NonNull AuthScreenState state) {
        super.onStateChanged(state);
        updateLogin(state.login());
        updatePassword(state.password());
        updateError(state.error());
    }

    @Override
    protected FragmentAuthBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentAuthBinding.inflate(inflater, container, false);
        binding.btnSignIn.setOnClickListener(this::onSingInClick);
        binding.tvSignUp.setOnClickListener(this::onSignUpClick);
        binding.etLogin.addTextChangedListener(SimpleTextWatcher.create(this::onLoginInput));
        binding.etPassword.addTextChangedListener(SimpleTextWatcher.create(this::onPasswordInput));
        return binding;
    }

    @NonNull
    @Override
    protected AuthViewModel createViewModel() {
        return new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void updateLogin(String login) {
        withBinding((binding) -> {
            if (!binding.etLogin.getText().toString().equals(login)) {
                binding.etLogin.setText(login);
            }
        });
    }

    private void updatePassword(String password) {
        withBinding((binding) -> {
            if (!binding.etPassword.getText().toString().equals(password)) {
                binding.etPassword.setText(password);
            }
        });
    }

    private void updateError(@Nullable AuthError error) {
        withBinding((binding) -> {
            final var tvError = binding.tvError;

            if (error instanceof InvalidCredentials) {
                tvError.setText("Неверные логин или пароль");
            } else if (error instanceof NotExistedUser) {
                tvError.setText("Пользователя с таким логином не существует");
            } else if (error instanceof Unknown) {
                tvError.setText("Ошибка");
            } else if (error == null) {
                tvError.setText("");
            }
        });
    }

    private void onSingInClick(View v) {
        getViewModel().onAction(new SignIn());
    }

    private void onSignUpClick(View v) {
        getViewModel().onAction(new SignUp());
    }

    private void onLoginInput(String text) {
        getViewModel().onAction(new LoginInput(text));
    }

    private void onPasswordInput(String text) {
        getViewModel().onAction(new PasswordInput(text));
    }
}