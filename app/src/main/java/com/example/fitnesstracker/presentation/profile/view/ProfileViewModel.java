package com.example.fitnesstracker.presentation.profile.view;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.profile.ProfileRepository;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.profile.edit.EditProfileFragment;
import com.example.fitnesstracker.presentation.profile.view.action.ProfileViewAction;
import com.example.fitnesstracker.presentation.singup.SingUpFragment;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends DisposableViewModel<User, ProfileViewAction> {
    private final @NonNull ProfileRepository profileRepository;
    private final @NonNull AuthRepository authRepository;
    private final @NonNull Router router;

    @Inject
    public ProfileViewModel(
            @NonNull ProfileRepository profileRepository,
            @NonNull AuthRepository authRepository,
            @NonNull Router router
    ) {
        super(new User("", "", "", 0, "", ""));

        this.profileRepository = profileRepository;
        this.authRepository = authRepository;
        this.router = router;
        observeUserState();
    }

    @Override
    public void onAction(@NonNull ProfileViewAction action) {
        if (action instanceof ProfileViewAction.Edit) {
            editProfile();
        } else if (action instanceof ProfileViewAction.Exit) {
            exit();
        }
    }

    private void exit() {
        authRepository
                .logout()
                .subscribe(
                        () -> router.newRootScreen(SingUpFragment.getScreen()),
                        (e) -> {},
                        disposables
                );

    }

    private void editProfile() {
        final var user = stateSubject.getValue();
        final var editScreen = EditProfileFragment.getScreen(user);
        router.navigateTo(editScreen);
    }

    private void observeUserState() {
        onSubscribe(() ->
                profileRepository
                        .observe()
                        .doOnNext(user -> updateState((state) -> user))
        );
    }
}
