package com.example.fitnesstracker.presentation.exercise.list.components;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitnesstracker.domain.workout.models.Exercise;

import java.util.function.Consumer;

public class ExerciseListAdapter extends ListAdapter<Exercise, ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Exercise> diffCallback = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.id().equals(newItem.id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final Consumer<Exercise> onClick;

    public ExerciseListAdapter(Consumer<Exercise> onClick) {
        super(diffCallback);
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ExerciseViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        final var item = getItem(position);
        holder.bind(item, (v) -> onClick.accept(item));
    }
}
