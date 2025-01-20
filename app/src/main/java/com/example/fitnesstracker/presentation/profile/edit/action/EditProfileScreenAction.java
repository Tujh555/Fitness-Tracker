package com.example.fitnesstracker.presentation.profile.edit.action;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.domain.User;

public interface EditProfileScreenAction {
    record Input(@NonNull String value, @NonNull ProfileField field) implements EditProfileScreenAction {}

    record UploadPhoto(@NonNull Uri uri) implements EditProfileScreenAction {}

    record AppendExisting(@NonNull User user) implements EditProfileScreenAction {}

    class Back implements EditProfileScreenAction {}

    class Save implements EditProfileScreenAction {}
}
