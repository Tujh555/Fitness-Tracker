package com.example.fitnesstracker.presentation.workout.create.components;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Pair;

import java.util.List;
import java.util.function.Consumer;

public class ExerciseListAdapter extends ListAdapter<Pair<Exercise, List<Pair<Integer, Integer>>>, ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Pair<Exercise, List<Pair<Integer, Integer>>>> diffCallback = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull Pair<Exercise, List<Pair<Integer, Integer>>> oldItem,
                @NonNull Pair<Exercise, List<Pair<Integer, Integer>>> newItem
        ) {
            return oldItem.first().id().equals(newItem.first().id());
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Pair<Exercise, List<Pair<Integer, Integer>>> oldItem,
                @NonNull Pair<Exercise, List<Pair<Integer, Integer>>> newItem
        ) {
            return oldItem.equals(newItem);
        }
    };
    private final @NonNull Consumer<Pair<Exercise, List<Pair<Integer, Integer>>>> onClick;

    public ExerciseListAdapter(@NonNull Consumer<Pair<Exercise, List<Pair<Integer, Integer>>>> onClick) {
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
        holder.bind(getItem(position), onClick);
    }
}
