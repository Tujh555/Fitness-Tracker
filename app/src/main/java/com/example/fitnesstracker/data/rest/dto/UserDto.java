package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.User;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.UUID;

public record UserDto(
        @SerializedName("id") Integer id,
        @SerializedName("name") String name,
        @SerializedName("login") String login,
        @SerializedName("age") Integer age,
        @SerializedName("avatar") String avatar,
        @SerializedName("target") String target
) {
    @NonNull
    @Contract(" -> new")
    public User toDomain() {
        return new User(
                Objects.requireNonNullElse(id(), 0),
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
