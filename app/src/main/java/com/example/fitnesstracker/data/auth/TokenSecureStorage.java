package com.example.fitnesstracker.data.auth;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.Storage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenSecureStorage implements Storage<String> {
    private final SharedPreferences preferences;
    private static final String tokenKey = "aut_token";

    @Inject
    public TokenSecureStorage(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
    }

    @Override
    public void save(@NonNull String item) {
        preferences
                .edit()
                .putString(tokenKey, item)
                .apply();
    }

    @Nullable
    @Override
    public String get() {
        return preferences.getString(tokenKey, null);
    }

    public void clear() {
        preferences
                .edit()
                .remove(tokenKey)
                .apply();
    }
}
