package com.example.fitnesstracker.presentation.auth.error;

public sealed interface AuthError permits NotExistedUser, InvalidCredentials, Unknown { }

