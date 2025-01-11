package com.example.fitnesstracker.data.profile;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.data.storage.Storage;
import com.example.fitnesstracker.data.rest.StreamRequestBody;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.data.rest.profile.ProfileApi;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.domain.profile.ProfileRepository;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class ProfileRepositoryImpl implements ProfileRepository {
    private final @NonNull Context context;
    private final @NonNull ProfileApi profileApi;
    private final @NonNull Storage<UserDto> userStorage;

    @Inject
    public ProfileRepositoryImpl(
            @NonNull @ApplicationContext Context context,
            @NonNull ProfileApi profileApi,
            @NonNull Storage<UserDto> userStorage
    ) {
        this.context = context;
        this.profileApi = profileApi;
        this.userStorage = userStorage;
    }

    @NonNull
    @Override
    public Single<User> uploadAvatar(@NonNull Uri uri) {
        final Single<MultipartBody.Part> createdBody = Single.create(emitter -> {
            try {
                final var stream = context
                        .getContentResolver()
                        .openInputStream(uri);
                final var body = new StreamRequestBody(stream);
                final var multipart = MultipartBody.Part.createFormData("photo", "", body);
                emitter.onSuccess(multipart);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        return createdBody
                .flatMap(profileApi::uploadAvatar)
                .doOnSuccess(userStorage::save)
                .map(UserDto::toDomain)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<User> editProfile(@NonNull User user) {
        return profileApi
                .editProfile(UserDto.of(user))
                .doOnSuccess(userStorage::save)
                .map(UserDto::toDomain)
                .subscribeOn(Schedulers.io());
    }
}
