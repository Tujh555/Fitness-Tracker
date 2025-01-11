package com.example.fitnesstracker.presentation.profile;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.profile.ProfileRepository;
import com.example.fitnesstracker.platform.UpdateProfilePhotoService;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.profile.action.ProfileField;
import com.example.fitnesstracker.presentation.profile.action.ProfileScreenAction;
import com.example.fitnesstracker.presentation.profile.state.ProfileScreenState;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class ProfileViewModel extends DisposableViewModel<ProfileScreenState, ProfileScreenAction> {
    private final @NonNull ProfileRepository repository;
    private final @NonNull Router router;
    private final @NonNull Context context;

    @Inject
    public ProfileViewModel(
            @NonNull ProfileRepository repository,
            @NonNull Router router,
            @NonNull @ApplicationContext Context context
    ) {
        super(ProfileScreenState.empty);
        this.repository = repository;
        this.router = router;
        this.context = context;
    }

    @Override
    public void onAction(@NonNull ProfileScreenAction action) {
        if (action instanceof ProfileScreenAction.Back) {
            router.exit();
        } else if (action instanceof ProfileScreenAction.Input input) {
            onInput(input.value(), input.field());
        } else if (action instanceof ProfileScreenAction.UploadPhoto upload) {
            uploadPhoto(upload.uri());
        }
    }

    private void onInput(@NonNull String value, @NonNull ProfileField field) {

    }

    private void uploadPhoto(@NonNull Uri uri) {
        UpdateProfilePhotoService.launch(context, uri);
    }
}
