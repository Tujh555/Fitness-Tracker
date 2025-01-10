package com.example.fitnesstracker.presentation.main.components;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.databinding.FragmentSignUpBinding;
import com.example.fitnesstracker.domain.workout.models.Workout;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    // TODO fix
    private final @NonNull FragmentSignUpBinding _binding;

    public WorkoutViewHolder(@NonNull FragmentSignUpBinding binding) {
        super(binding.getRoot());
        _binding = binding;
    }

    public void bind(Workout workout) {
        // TODO
    }

    @NonNull
    public static WorkoutViewHolder create(ViewGroup parent) {
        final var binding = FragmentSignUpBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new WorkoutViewHolder(binding);
    }
}
