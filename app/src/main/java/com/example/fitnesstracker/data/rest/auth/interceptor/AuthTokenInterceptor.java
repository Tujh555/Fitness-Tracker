package com.example.fitnesstracker.data.rest.auth.interceptor;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.data.storage.Storage;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthTokenInterceptor implements Interceptor {
    private final Storage<String> storage;

    @Inject
    public AuthTokenInterceptor(Storage<String> secureStorage) {
        storage = secureStorage;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final var builder = chain.request().newBuilder();

        final var token = storage.get();

        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
