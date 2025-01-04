package com.example.fitnesstracker.presentation.auth.action;

import androidx.annotation.NonNull;

public record LoginInput(@NonNull String login) implements AuthScreenAction {}
