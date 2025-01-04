package com.example.fitnesstracker.presentation.auth.action;

public sealed interface AuthScreenAction permits LoginInput, PasswordInput, SignIn, SignUp { }

