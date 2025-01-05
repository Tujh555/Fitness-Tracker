package com.example.fitnesstracker.presentation.singup;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.databinding.FragmentSignUpBinding;
import com.example.fitnesstracker.presentation.SimpleTextWatcher;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.singup.action.AgeInput;
import com.example.fitnesstracker.presentation.singup.action.LoginInput;
import com.example.fitnesstracker.presentation.singup.action.NameInput;
import com.example.fitnesstracker.presentation.singup.action.PasswordInput;
import com.example.fitnesstracker.presentation.singup.action.SignUp;
import com.example.fitnesstracker.presentation.singup.action.SignUpScreenAction;
import com.example.fitnesstracker.presentation.singup.state.SignUpScreenState;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SingUpFragment extends BaseFragment<SignUpScreenState, SignUpScreenAction, FragmentSignUpBinding, SignUpViewModel> {
    @Override
    protected FragmentSignUpBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentSignUpBinding.inflate(inflater, container, false);

        binding.etAge.addTextChangedListener(
                SimpleTextWatcher.create(text -> onAction(new AgeInput(text)))
        );
        binding.etLogin.addTextChangedListener(
                SimpleTextWatcher.create(text -> onAction(new LoginInput(text)))
        );
        binding.etName.addTextChangedListener(
                SimpleTextWatcher.create(text -> onAction(new NameInput(text)))
        );
        binding.etPassword.addTextChangedListener(
                SimpleTextWatcher.create(text -> onAction(new PasswordInput(text)))
        );
        binding.btnSignUp.setOnClickListener((v) -> onAction(new SignUp()));

        return binding;
    }

    @NonNull
    @Override
    protected SignUpViewModel createViewModel() {
        return new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    @Override
    protected void onStateChanged(@NonNull SignUpScreenState state) {
        super.onStateChanged(state);
        updateError(state.isError());
        withBinding(binding -> {
            updateEditText(binding.etAge, state.age());
            updateEditText(binding.etName, state.name());
            updateEditText(binding.etLogin, state.login());
            updateEditText(binding.etPassword, state.password());
        });
    }

    private void updateEditText(@NonNull EditText editText, String text) {
        if (!editText.getText().toString().equals(text)) {
            editText.setText(text);
        }
    }

    private void updateError(boolean isError) {
        withBinding(binding -> {
            if (isError) {
                binding.tvError.setText("Ошибка");
            } else {
                binding.tvError.setText("");
            }
            binding.btnSignUp.setActivated(!isError);
        });
    }

    @NonNull
    @Contract(" -> new")
    public static FragmentScreen getScreen() {
        return FragmentScreen
                .Companion
                .invoke(null, true, (f) -> new SingUpFragment());
    }
}
