package com.example.fitnesstracker.data.rest.auth.interceptor;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.data.auth.TokenSecureStorage;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthTokenInterceptor implements Interceptor {
    private final TokenSecureStorage storage;

    @Inject
    public AuthTokenInterceptor(TokenSecureStorage secureStorage) {
        storage = secureStorage;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final var builder = chain.request().newBuilder();

        final var token = storage.getToken();

        if (token != null) {
            builder.addHeader("Authorization", "Basic " + token);
        }

        return chain.proceed(builder.build());
    }
}
