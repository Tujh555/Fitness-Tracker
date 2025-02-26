package com.example.fitnesstracker.data.rest;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.data.rest.auth.AuthApi;
import com.example.fitnesstracker.data.rest.auth.interceptor.AuthTokenInterceptor;
import com.example.fitnesstracker.data.rest.profile.ProfileApi;
import com.example.fitnesstracker.data.rest.workout.WorkoutApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApiProvider {
    @Provides
    @Singleton
    public Retrofit provideRetrofit(AuthTokenInterceptor authTokenInterceptor) {
        final var logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        final var client = new OkHttpClient.Builder()
                .addInterceptor(authTokenInterceptor)
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    public AuthApi provideAuthApi(@NonNull Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

    @Provides
    public WorkoutApi provideWorkoutApi(@NonNull Retrofit retrofit) {
        return retrofit.create(WorkoutApi.class);
    }

    @Provides
    public ProfileApi profileApi(@NonNull Retrofit retrofit) {
        return retrofit.create(ProfileApi.class);
    }
}
