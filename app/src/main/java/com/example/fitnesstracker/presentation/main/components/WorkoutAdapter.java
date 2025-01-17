package com.example.fitnesstracker.presentation.main.components;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitnesstracker.domain.workout.models.Workout;

import java.util.function.Consumer;

public class WorkoutAdapter extends ListAdapter<Workout, WorkoutViewHolder> {
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
    private final @NonNull Consumer<Workout> onLongClick;

    public WorkoutAdapter(@NonNull Consumer<Workout> onLongClick) {
        super(comparator);
        this.onLongClick = onLongClick;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WorkoutViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        final var item = getItem(position);

        if (item != null) {
            holder.bind(item, onLongClick);
        }
    }
}
