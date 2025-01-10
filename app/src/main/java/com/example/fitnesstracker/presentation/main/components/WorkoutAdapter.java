package com.example.fitnesstracker.presentation.main.components;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.fitnesstracker.domain.workout.models.Workout;

public class WorkoutAdapter extends PagingDataAdapter<Workout, WorkoutViewHolder> {
    private static final DiffUtil.ItemCallback<Workout> comparator = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.id().equals(newItem.id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.equals(newItem);
        }
    };

    public WorkoutAdapter() { super(comparator); }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WorkoutViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        final var item = getItem(position);

        if (item != null) {
            holder.bind(item);
        }
    }
}
