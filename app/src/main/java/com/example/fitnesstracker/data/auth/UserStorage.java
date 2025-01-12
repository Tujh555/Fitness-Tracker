package com.example.fitnesstracker.data.auth;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.storage.ObservableStorage;
import com.example.fitnesstracker.data.storage.Storage;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.domain.LazyField;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class UserStorage extends ObservableStorage<UserDto> {
    private static final String key = "user";
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    @Inject
    public UserStorage(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
        preferences.registerOnSharedPreferenceChangeListener(this::onPrefsChanged);
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

    private void onPrefsChanged(SharedPreferences preferences, String key) {
        final var user = get();
        if (user != null) {
            update(user);
        }
    }
}
