package com.example.fitnesstracker.presentation.exercise.edit.action;

import android.net.Uri;

import androidx.annotation.NonNull;

public interface ExerciseAppendScreenAction {
    class Cancel implements ExerciseAppendScreenAction {}

    class Save implements ExerciseAppendScreenAction {}

    record TitleInput(@NonNull String text) implements ExerciseAppendScreenAction {}

    record UriReceived(@NonNull Uri uri) implements ExerciseAppendScreenAction {}

    record IdReceived(@NonNull String id) implements ExerciseAppendScreenAction {}
}
