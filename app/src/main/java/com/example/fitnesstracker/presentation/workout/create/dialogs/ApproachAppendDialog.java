package com.example.fitnesstracker.presentation.workout.create.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.DialogAppendApproachBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;

public class ApproachAppendDialog extends DialogFragment {
    private static final @NonNull String exerciseKey = "exercise";
    private @Nullable DialogAppendApproachBinding binding;
    private Exercise exercise;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        final var _binding = DialogAppendApproachBinding.inflate(inflater, container, false);

        final var args = getArguments();

        if (args == null) {
            throw new IllegalStateException("Exercise must be put");
        }

        final var e = (Exercise) args.getSerializable(exerciseKey);

        if (e == null) {
            throw new IllegalStateException("Exercise must be put");
        }
        exercise = e;

        _binding.tvTitle.setText(exercise.title());

        Glide.with(this)
                .load(exercise.describingPhoto())
                .placeholder(R.drawable.ic_workout)
                .centerCrop()
                .into(_binding.ivDescribingPhoto);

        _binding.btnOk.setOnClickListener(this::onPositiveClick);
        _binding.btnCancel.setOnClickListener(v -> dismiss());

        binding = _binding;
        return _binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onPositiveClick(View v) {
        if (binding == null) {
            return;
        }
        Log.e("--tag", "CLICK");

        if (getParentFragment() instanceof PositiveClickListener listener) {
            int repetitions;
            int weight;

            try {
                repetitions = Integer.parseInt(binding.etRepetitions.getText().toString());
                weight = Integer.parseInt(binding.etWeight.getText().toString());
            } catch (Exception e) {
                repetitions = 1;
                weight = 10;
            }

            listener.addExercise(exercise, repetitions, weight);
            dismiss();
        }
    }

    @FunctionalInterface
    public interface PositiveClickListener {
        void addExercise(@NonNull Exercise exercise, int repetitions, int weight);
    }

    @NonNull
    public static ApproachAppendDialog create(@NonNull Exercise exercise) {
        final var dialog = new ApproachAppendDialog();
        final var args = new Bundle();
        args.putSerializable(exerciseKey, exercise);
        dialog.setArguments(args);

        return dialog;
    }
}
