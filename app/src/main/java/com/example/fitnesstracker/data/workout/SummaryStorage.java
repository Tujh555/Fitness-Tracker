package com.example.fitnesstracker.data.workout;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.data.rest.dto.SummaryDto;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.data.storage.ObservableStorage;
import com.example.fitnesstracker.domain.LazyField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class SummaryStorage extends ObservableStorage<List<SummaryDto>> {
    private static final String key = "summary";
    private static final Type listType = TypeToken
            .getParameterized(List.class, SummaryDto.class)
            .getType();
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    @Inject
    public SummaryStorage(SharedPreferences preferences) {
        this.preferences = preferences;
        preferences.registerOnSharedPreferenceChangeListener(this::onPrefsChanged);
    }

    @Override
    public void save(@NonNull List<SummaryDto> item) {
        final var json = gson.toJson(item, listType);
        preferences.edit().putString(key, json).apply();
    }

    @Nullable
    @Override
    public List<SummaryDto> get() {
        final var json = preferences.getString(key, null);
        if (json == null) {
            return null;
        }

        try {
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clear() {
        preferences.edit().remove(key).apply();
    }

    private void onPrefsChanged(SharedPreferences preferences, String key) {
        final var item = get();
        if (item != null) {
            update(item);
        }
    }
}
