package com.example.fitnesstracker.presentation.profile.edit;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.PickVisualMediaRequestKt;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.FragmentEditProfileBinding;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.presentation.SimpleTextWatcher;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.profile.edit.action.EditProfileScreenAction;
import com.example.fitnesstracker.presentation.profile.edit.action.ProfileField;
import com.example.fitnesstracker.presentation.profile.edit.state.EditProfileScreenState;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProfileFragment extends BaseFragment<EditProfileScreenState, EditProfileScreenAction, FragmentEditProfileBinding, EditProfileViewModel> {
    private static final String userKey = "user";
    private ActivityResultLauncher<PickVisualMediaRequest> pickImage;

    @Override
    protected FragmentEditProfileBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            final var user = (User) getArguments().getSerializable(userKey);
            if (user != null) {
                initViews(binding, user);
            }
        }

        binding.etTarget.addTextChangedListener(
                SimpleTextWatcher.create(text ->
                        onAction(new EditProfileScreenAction.Input(text, ProfileField.TARGET))
                )
        );

        binding.etAge.addTextChangedListener(
                SimpleTextWatcher.create(text ->
                        onAction(new EditProfileScreenAction.Input(text, ProfileField.AGE))
                )
        );

        binding.etName.addTextChangedListener(
                SimpleTextWatcher.create(text ->
                        onAction(new EditProfileScreenAction.Input(text, ProfileField.NAME))
                )
        );

        binding.etLogin.addTextChangedListener(
                SimpleTextWatcher.create(text ->
                        onAction(new EditProfileScreenAction.Input(text, ProfileField.LOGIN))
                )
        );

        binding.btnSave.setOnClickListener((v) -> onAction(new EditProfileScreenAction.Save()));

        pickImage = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> onAction(new EditProfileScreenAction.UploadPhoto(uri))
        );
        binding.ivAvatar.setOnClickListener((v) -> {
            final var request = PickVisualMediaRequestKt.PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE
            );
            pickImage.launch(request);
        });

        return binding;
    }

    @Override
    protected void onStateChanged(@NonNull EditProfileScreenState state) {
        super.onStateChanged(state);
        withBinding(binding -> {
            updateEditText(binding.etAge, state.age());
            updateEditText(binding.etLogin, state.login());
            updateEditText(binding.etName, state.name());
            updateEditText(binding.etTarget, state.target());
        });
    }

    @NonNull
    @Override
    protected EditProfileViewModel createViewModel() {
        return new ViewModelProvider(this).get(EditProfileViewModel.class);
    }

    private void initViews(@NonNull FragmentEditProfileBinding binding, @NonNull User initialUser) {
        if (initialUser.avatar() != null) {
            Glide.with(this)
                    .load(initialUser.avatar())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(binding.ivAvatar);
        }

        binding.etName.setText(initialUser.name());
        binding.etLogin.setText(initialUser.login());
        if (initialUser.age() != null) {
            binding.etAge.setText(initialUser.age().toString());
        }
        if (initialUser.target() != null) {
            binding.etTarget.setText(initialUser.target());
        }
    }

    private void updateEditText(@NonNull EditText editText, String text) {
        if (!editText.getText().toString().equals(text)) {
            editText.setText(text);
        }
    }

    @NonNull
    @Contract(" -> new")
    public static FragmentScreen getScreen(@NonNull User user) {
        return FragmentScreen
                .Companion
                .invoke(
                        null,
                        true,
                        (f) -> {
                            final var fragment = new EditProfileFragment();
                            final var arguments = new Bundle();
                            arguments.putSerializable(userKey, user);
                            fragment.setArguments(arguments);
                            return fragment;
                        }
                );
    }
}
