package com.example.fitnesstracker.presentation.profile.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.profile.ProfileRepository;
import com.example.fitnesstracker.platform.UpdateProfilePhotoService;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.profile.edit.action.ProfileField;
import com.example.fitnesstracker.presentation.profile.edit.action.EditProfileScreenAction;
import com.example.fitnesstracker.presentation.profile.edit.state.EditProfileScreenState;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@HiltViewModel
public class EditProfileViewModel extends DisposableViewModel<EditProfileScreenState, EditProfileScreenAction> {
    private final @NonNull ProfileRepository repository;
    private final @NonNull Router router;
    private final @NonNull Context context;

    @Inject
    public EditProfileViewModel(
            @NonNull ProfileRepository repository,
            @NonNull Router router,
            @NonNull @ApplicationContext Context context
    ) {
        super(EditProfileScreenState.empty);
        this.repository = repository;
        this.router = router;
        this.context = context;
    }

    @Override
    public void onAction(@NonNull EditProfileScreenAction action) {
        if (action instanceof EditProfileScreenAction.Back) {
            router.exit();
        } else if (action instanceof EditProfileScreenAction.Input input) {
            onInput(input.value(), input.field());
        } else if (action instanceof EditProfileScreenAction.UploadPhoto upload) {
            uploadPhoto(upload.uri());
        } else if (action instanceof EditProfileScreenAction.Save) {
            save();
        } else if (action instanceof EditProfileScreenAction.AppendExisting appendExisting) {
            appendExisting(appendExisting.user());
        }
    }

    private void appendExisting(@NonNull User user) {
        updateState(state -> {
            final var age = user.age() == null ? "" : user.age().toString();
            final var target = user.target() == null ? "" : user.target();

            return new EditProfileScreenState(
                    user.avatar(),
                    user.name(),
                    user.login(),
                    age,
                    target
            );
        });
    }

    @SuppressLint("CheckResult")
    private void save() {
        final var state = stateSubject.getValue();
        Integer age;

        try {
            age = Integer.parseInt(state.age());
        } catch (NumberFormatException e) {
            age = null;
        }

        repository
                .editProfile(state.name(), state.login(), age, state.target())
                .subscribe((v) -> {}, (e) -> {});

        router.exit();
    }

    private void onInput(@NonNull String value, @NonNull ProfileField field) {
        updateState(state -> switch (field) {
            case NAME -> new EditProfileScreenState(
                    state.avatar(),
                    value,
                    state.login(),
                    state.age(),
                    state.target()
            );
            case LOGIN -> new EditProfileScreenState(
                    state.avatar(),
                    state.name(),
                    value,
                    state.age(),
                    state.target()
            );
            case AGE -> new EditProfileScreenState(
                    state.avatar(),
                    state.name(),
                    state.login(),
                    value,
                    state.target()
            );
            case TARGET -> new EditProfileScreenState(
                    state.avatar(),
                    state.name(),
                    state.login(),
                    state.age(),
                    value
            );
        });
    }

    private void uploadPhoto(@NonNull Uri uri) {
        UpdateProfilePhotoService.launch(context, uri);
        router.exit();
    }
}
