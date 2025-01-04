package com.example.fitnesstracker.presentation.auth.action;

import androidx.annotation.NonNull;

public record PasswordInput(@NonNull String password) implements AuthScreenAction {}
