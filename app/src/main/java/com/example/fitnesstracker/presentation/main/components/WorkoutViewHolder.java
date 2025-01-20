package com.example.fitnesstracker.presentation.main.components;

import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.ExerciseWithApproachesListItemBinding;
import com.example.fitnesstracker.databinding.WorkoutListItemBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    private final @NonNull DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("HH:mm; dd MMM yyyy", Locale.getDefault())
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

    public void bind(@NonNull Workout workout, @NonNull Consumer<Workout> onLongClick) {
        _binding.getRoot().setOnLongClickListener(view -> {
            view.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            );
            onLongClick.accept(workout);
            return true;
        });

        _binding.tvTitle.setText(workout.title());
        _binding.tvDate.setText(formatter.format(workout.date()));

        final var views = workout
                .exercises()
                .stream()
                .map(exercise -> {
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

                    return exerciseBinding.getRoot();
                })
                .collect(Collectors.toList());

        final var adapter = new ViewsAdapter(views);
        _binding.lvExercises.setAdapter(adapter);
    }

    private static final class ViewsAdapter extends BaseAdapter {
        private final @NonNull List<? extends View> views;

        private ViewsAdapter(@NonNull List<? extends View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object getItem(int i) {
            return views.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return views.get(i);
        }
    }

    @NonNull
    public static WorkoutViewHolder create(@NonNull ViewGroup parent) {
        final var layoutInflater = LayoutInflater.from(parent.getContext());
        final var binding = WorkoutListItemBinding.inflate(layoutInflater, parent, false);

        return new WorkoutViewHolder(binding, layoutInflater, parent);
    }
}
