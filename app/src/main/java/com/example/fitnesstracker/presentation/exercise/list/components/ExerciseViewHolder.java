package com.example.fitnesstracker.presentation.exercise.list.components;

import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.ExerciseListItemBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;

import java.util.function.Consumer;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final @NonNull ExerciseListItemBinding _binding;

    public ExerciseViewHolder(@NonNull ExerciseListItemBinding binding) {
        super(binding.getRoot());
        _binding = binding;
    }

    public void bind(@NonNull Exercise exercise, @NonNull Consumer<View> onClick) {
        _binding.getRoot().setOnLongClickListener(view -> {
            view.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            );
            onClick.accept(view);
            return true;
        });

        Glide.with(_binding.getRoot())
                .load(exercise.describingPhoto())
                .placeholder(R.drawable.ic_workout)
                .centerCrop()
                .into(_binding.ivDescribingPhoto);

        _binding.tvTitle.setText(exercise.title());
    }

    @NonNull
    public static ExerciseViewHolder create(ViewGroup parent) {
        final var binding = ExerciseListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ExerciseViewHolder(binding);
    }
}
