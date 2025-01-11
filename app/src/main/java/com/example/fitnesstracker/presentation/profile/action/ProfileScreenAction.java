package com.example.fitnesstracker.presentation.profile.action;

import android.net.Uri;

import androidx.annotation.NonNull;

public interface ProfileScreenAction {
    record Input(@NonNull String value, @NonNull ProfileField field) implements ProfileScreenAction {}

    record UploadPhoto(@NonNull Uri uri) implements ProfileScreenAction {}

    class Back implements ProfileScreenAction {}
}
