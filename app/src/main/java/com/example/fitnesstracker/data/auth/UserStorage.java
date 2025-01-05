package com.example.fitnesstracker.data.auth;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.Storage;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserStorage implements Storage<UserDto> {
    private static final String key = "user";
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    @Inject
    public UserStorage(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
    }

    @Override
    public void save(@NonNull UserDto item) {
        preferences
                .edit()
                .putString(key, gson.toJson(item))
                .apply();
    }

    @Nullable
    @Override
    public UserDto get() {
        final var json = preferences.getString(key, null);

        if (json == null) {
            return null;
        }

        try {
            return gson.fromJson(json, UserDto.class);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public void clear() {
        preferences
                .edit()
                .remove(key)
                .apply();
    }
}
