package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.User;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.UUID;

public record UserDto(
        @Nullable @SerializedName("id") Integer id,
        @Nullable @SerializedName("name") String name,
        @Nullable @SerializedName("login") String login,
        @Nullable @SerializedName("age") Integer age,
        @Nullable @SerializedName("avatar") String avatar,
        @Nullable @SerializedName("target") String target
) {
    @NonNull
    @Contract(" -> new")
    public User toDomain() {
        return new User(
                Objects.requireNonNullElse(id(), Integer.valueOf(0)),
                Objects.requireNonNullElse(name(), "Unknown"),
                Objects.requireNonNullElse(login(), "unknown"),
                Objects.requireNonNullElse(age(), 18),
                Objects.requireNonNullElse(avatar(), ""),
                Objects.requireNonNullElse(target(), "")
        );
    }

    @NonNull
    public static UserDto of(@NonNull User user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.login(),
                user.age(),
                user.avatar(),
                user.target()
        );
    }
}
