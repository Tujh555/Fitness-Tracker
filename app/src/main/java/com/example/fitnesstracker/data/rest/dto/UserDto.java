package com.example.fitnesstracker.data.rest.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnesstracker.domain.User;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.UUID;

public record UserDto(
        @Nullable @SerializedName("id") String id,
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
                Objects.requireNonNullElse(id(), UUID.randomUUID().toString()),
                Objects.requireNonNullElse(name(), "Unknown"),
                Objects.requireNonNullElse(login(), "unknown"),
                Objects.requireNonNullElse(age(), 18),
                Objects.requireNonNullElse(avatar(), ""),
                Objects.requireNonNullElse(target(), "")
        );
    }
}
