package com.example.fitnesstracker.data.di;

import com.example.fitnesstracker.data.auth.AuthRepositoryImpl;
import com.example.fitnesstracker.domain.auth.AuthRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
interface DataModule {
    @Binds
    AuthRepository bind(AuthRepositoryImpl impl);
}
