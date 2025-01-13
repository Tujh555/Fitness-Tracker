package com.example.fitnesstracker.presentation.main.components;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.ExerciseWithApproachesListItemBinding;
import com.example.fitnesstracker.databinding.WorkoutListItemBinding;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    private final @NonNull DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd MMM yyyy HH:mm", Locale.getDefault())
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    private final @NonNull WorkoutListItemBinding _binding;
    private final @NonNull LayoutInflater layoutInflater;
    private final @NonNull ViewGroup parent;

    public WorkoutViewHolder(
            @NonNull WorkoutListItemBinding binding,
            @NonNull LayoutInflater inflater,
            @NonNull ViewGroup parent
    ) {
        super(binding.getRoot());
        _binding = binding;
        layoutInflater = inflater;
        this.parent = parent;
    }

    public void bind(@NonNull Workout workout) {
        _binding.tvTitle.setText(workout.title());
        _binding.tvDate.setText(formatter.format(workout.date()));

        workout.exercises().forEach(exercise -> {
            final var exerciseBinding = ExerciseWithApproachesListItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
            );
            exerciseBinding.tvTitle.setText(exercise.title());

            Glide.with(_binding.getRoot().getContext())
                    .load(exercise.describingPhoto())
                    .placeholder(R.drawable.ic_workout)
                    .centerCrop()
                    .into(exerciseBinding.ivDescribingPhoto);

            final var approaches = workout.approaches().get(exercise.id());

            if (approaches != null) {
                final var text = new StringBuilder();

                approaches.forEach(approach ->
                        text.append(approach.repetitions())
                                .append(" по ")
                                .append(approach.weight())
                                .append('\n')
                );

                exerciseBinding.tvApproaches.setText(text.toString());
            }

            _binding.lvExercises.addView(exerciseBinding.getRoot());
        });
    }

    @NonNull
    public static WorkoutViewHolder create(@NonNull ViewGroup parent) {
        final var layoutInflater = LayoutInflater.from(parent.getContext());
        final var binding = WorkoutListItemBinding.inflate(layoutInflater, parent, false);

        return new WorkoutViewHolder(binding, layoutInflater, parent);
    }
}
