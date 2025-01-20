package com.example.fitnesstracker.presentation.workout.create.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesstracker.databinding.DialogExerciseSelectBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.presentation.exercise.list.components.ExerciseListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.lifecycle.HiltViewModel;

@AndroidEntryPoint
public class ExerciseSelectDialog extends DialogFragment {
    private @NonNull List<Exercise> exercises = new ArrayList<>();
    private ExerciseListAdapter adapter;

    public ExerciseSelectDialog(@NonNull List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public ExerciseSelectDialog() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        final var binding = DialogExerciseSelectBinding.inflate(inflater, container, false);
        final var root = binding.getRoot();
        final var viewModel = new ViewModelProvider(this).get(ExerciseSelectViewModel.class);

        if (!exercises.isEmpty()) {
            viewModel.exercises = exercises;
        }
        adapter = new ExerciseListAdapter(exercise -> {
            if (getParentFragment() instanceof ExerciseSelectedListener listener) {
                listener.onSelected(exercise);
                dismiss();
            }
        });

        root.setAdapter(adapter);
        root.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        final var viewModel = new ViewModelProvider(this).get(ExerciseSelectViewModel.class);
        adapter.submitList(viewModel.exercises);
    }

    @FunctionalInterface
    public interface ExerciseSelectedListener {
        void onSelected(@NonNull Exercise exercise);
    }

    @HiltViewModel
    static class ExerciseSelectViewModel extends ViewModel {
        @Inject
        public ExerciseSelectViewModel() {}

        public List<Exercise> exercises = new ArrayList<>();
    }
}
