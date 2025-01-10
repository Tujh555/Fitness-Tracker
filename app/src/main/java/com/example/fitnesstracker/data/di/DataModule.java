package com.example.fitnesstracker.data.di;

import com.example.fitnesstracker.data.Storage;
import com.example.fitnesstracker.data.auth.AuthRepositoryImpl;
import com.example.fitnesstracker.data.auth.TokenSecureStorage;
import com.example.fitnesstracker.data.auth.UserStorage;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.data.workout.WorkoutRepositoryImpl;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.workout.WorkoutRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
interface DataModule {
    @Binds
    AuthRepository bind(AuthRepositoryImpl impl);

    @Binds
    Storage<UserDto> bindUserStorage(UserStorage storage);

    @Binds
    Storage<String> bindAuthTokenStorage(TokenSecureStorage storage);

    @Binds
    WorkoutRepository bindWorkout(WorkoutRepositoryImpl impl);
}
