package com.example.fitnesstracker.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public record User(
        @NonNull Integer id,
        @NonNull String name,
        @NonNull String login,
        @Nullable Integer age,
        @Nullable String avatar,
        @Nullable String target
) implements Serializable { }
