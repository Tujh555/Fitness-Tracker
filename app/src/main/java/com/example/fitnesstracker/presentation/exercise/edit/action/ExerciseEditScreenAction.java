package com.example.fitnesstracker.presentation.exercise.edit.action;

import android.net.Uri;

import androidx.annotation.NonNull;

public interface ExerciseEditScreenAction {
    class Cancel implements ExerciseEditScreenAction {}

    class Save implements ExerciseEditScreenAction {}

    record TitleInput(@NonNull String text) implements ExerciseEditScreenAction {}

    record UriReceived(@NonNull Uri uri) implements ExerciseEditScreenAction {}

    record IdReceived(@NonNull String id) implements ExerciseEditScreenAction {}
}
