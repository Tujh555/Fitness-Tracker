package com.example.fitnesstracker.presentation.exercise.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesstracker.databinding.FragmentExerciseListBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.exercise.list.components.ExerciseListAdapter;

import java.util.List;

public class ExerciseListFragment extends BaseFragment<List<Exercise>, ExerciseListScreenAction, FragmentExerciseListBinding, ExerciseListViewModel> {
    private ExerciseListAdapter exerciseAdapter;

    @Override
    protected FragmentExerciseListBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentExerciseListBinding.inflate(inflater, container, false);

        exerciseAdapter = new ExerciseListAdapter(e -> onAction(new ExerciseListScreenAction.Edit(e)));
        final var layoutManager = new LinearLayoutManager(
                binding.getRoot().getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        binding.rvExercises.setAdapter(exerciseAdapter);
        binding.rvExercises.setLayoutManager(layoutManager);

        return binding;
    }

    @Override
    protected void onStateChanged(@NonNull List<Exercise> state) {
        super.onStateChanged(state);
        exerciseAdapter.submitList(state);
    }

    @NonNull
    @Override
    protected ExerciseListViewModel createViewModel() {
        return new ViewModelProvider(this).get(ExerciseListViewModel.class);
    }
}
