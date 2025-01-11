package com.example.fitnesstracker.data.di;

import com.example.fitnesstracker.data.storage.ObservableStorage;
import com.example.fitnesstracker.data.storage.Storage;
import com.example.fitnesstracker.data.auth.AuthRepositoryImpl;
import com.example.fitnesstracker.data.auth.TokenSecureStorage;
import com.example.fitnesstracker.data.auth.UserStorage;
import com.example.fitnesstracker.data.profile.ProfileRepositoryImpl;
import com.example.fitnesstracker.data.rest.dto.UserDto;
import com.example.fitnesstracker.data.workout.WorkoutRepositoryImpl;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.profile.ProfileRepository;
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
    ObservableStorage<UserDto> bindUserObservableStorage(UserStorage storage);

    @Binds
    Storage<String> bindAuthTokenStorage(TokenSecureStorage storage);

    @Binds
    WorkoutRepository bindWorkout(WorkoutRepositoryImpl impl);

    @Binds
    ProfileRepository bindProfile(ProfileRepositoryImpl impl);
}
