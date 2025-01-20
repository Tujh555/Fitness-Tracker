package com.example.fitnesstracker.presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.auth.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final @NonNull AuthRepository repository;

    @Inject
    public MainViewModel(@NonNull AuthRepository repository) {
        this.repository = repository;
    }

    public Single<User> observeExistingUser() {
        return repository.getExisting();
    }
}
