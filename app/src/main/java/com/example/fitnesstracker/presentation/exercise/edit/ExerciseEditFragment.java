package com.example.fitnesstracker.presentation.exercise.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

public class ExerciseEditFragment extends Fragment {
    private static final String existingExerciseKey = "exercise";

    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen(@Nullable Exercise exercise) {
        return FragmentScreen.Companion.invoke(null, true, f -> {
            final var fragment = new ExerciseEditFragment();

            if (exercise != null) {
                final var args = new Bundle();
                args.putSerializable(existingExerciseKey, exercise);
                fragment.setArguments(args);
            }

            return fragment;
        });
    }
}
