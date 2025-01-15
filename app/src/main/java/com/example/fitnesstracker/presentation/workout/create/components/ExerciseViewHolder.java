package com.example.fitnesstracker.presentation.workout.create.components;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.ExerciseWithApproachesListItemBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;

import java.util.List;
import java.util.function.Consumer;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final @NonNull ExerciseWithApproachesListItemBinding binding;

    public ExerciseViewHolder(@NonNull ExerciseWithApproachesListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(
            @NonNull Pair<Exercise, List<Pair<Integer, Integer>>> exercisePair,
            @NonNull Consumer<Pair<Exercise, List<Pair<Integer, Integer>>>> onClick
    ) {
        final var exercise = exercisePair.first();

        binding.getRoot().setOnClickListener(v -> onClick.accept(exercisePair));

        binding.tvTitle.setText(exercise.title());

        Glide.with(binding.getRoot().getContext())
                .load(exercise.describingPhoto())
                .placeholder(R.drawable.ic_workout)
                .centerCrop()
                .into(binding.ivDescribingPhoto);

        final var approachText = new StringBuilder();

        exercisePair.second().forEach(approach ->
                approachText.append(approach.first())
                        .append(" по ")
                        .append(approach.second())
                        .append('\n')
        );

        binding.tvApproaches.setText(approachText);
    }

    @NonNull
    public static ExerciseViewHolder create(@NonNull ViewGroup parent) {
        final var inflater = LayoutInflater.from(parent.getContext());
        final var binding = ExerciseWithApproachesListItemBinding.inflate(inflater, parent, false);

        return new ExerciseViewHolder(binding);
    }
}
