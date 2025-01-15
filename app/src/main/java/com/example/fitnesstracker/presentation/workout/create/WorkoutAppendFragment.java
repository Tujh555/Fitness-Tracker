package com.example.fitnesstracker.presentation.workout.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesstracker.databinding.FragmentWorkoutAppendBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.domain.workout.models.Workout;
import com.example.fitnesstracker.presentation.SimpleTextWatcher;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.workout.create.action.WorkoutAppendScreenAction;
import com.example.fitnesstracker.presentation.workout.create.components.ExerciseListAdapter;
import com.example.fitnesstracker.presentation.workout.create.dialogs.ApproachAppendDialog;
import com.example.fitnesstracker.presentation.workout.create.dialogs.DatePickerFactory;
import com.example.fitnesstracker.presentation.workout.create.dialogs.ExerciseSelectDialog;
import com.example.fitnesstracker.presentation.workout.create.state.WorkoutAppendScreenState;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WorkoutAppendFragment
        extends BaseFragment<WorkoutAppendScreenState, WorkoutAppendScreenAction, FragmentWorkoutAppendBinding, WorkoutAppendViewModel>
        implements ApproachAppendDialog.PositiveClickListener, ExerciseSelectDialog.ExerciseSelectedListener {

    private static final @NonNull String existingWorkoutKey = "workout";
    private ExerciseListAdapter exerciseListAdapter;

    @Override
    protected FragmentWorkoutAppendBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentWorkoutAppendBinding.inflate(inflater, container, false);

        exerciseListAdapter = new ExerciseListAdapter(exercisePair ->
                showApproachDialog(exercisePair.first())
        );
        binding.rvExercises.setAdapter(exerciseListAdapter);
        binding.rvExercises.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        final var args = getArguments();
        if (args != null) {
            final var existing = (Workout) args.getSerializable(existingWorkoutKey);
            if (existing != null) {
                onAction(new WorkoutAppendScreenAction.ApplyExisting(existing));
            }
        }

        binding.btnDate.setOnClickListener(v -> {
            final var datePickerDialog = DatePickerFactory.date(calendar -> {
                final var timePicker = DatePickerFactory.time(calendar, time ->
                        onAction(new WorkoutAppendScreenAction.SetDate(time))
                );
                timePicker.show(getParentFragmentManager(), "time");
            });

            datePickerDialog.show(getParentFragmentManager(), "date");
        });

        binding.etTitle.addTextChangedListener(
                SimpleTextWatcher.create(text -> onAction(new WorkoutAppendScreenAction.Title(text)))
        );

        binding.btnAddExercise.setOnClickListener(v -> {
            final var dialog = new ExerciseSelectDialog(getViewModel().availableExercises());
            dialog.show(getParentFragmentManager(), "");
        });

        binding.btnSave.setOnClickListener(v -> onAction(new WorkoutAppendScreenAction.Save()));
        binding.btnClear.setOnClickListener(v -> onAction(new WorkoutAppendScreenAction.Clear()));
        return binding;
    }

    @NonNull
    @Override
    protected WorkoutAppendViewModel createViewModel() {
        return new ViewModelProvider(this).get(WorkoutAppendViewModel.class);
    }

    @Override
    protected void onStateChanged(@NonNull WorkoutAppendScreenState state) {
        super.onStateChanged(state);
        withBinding(binding -> {
            updateEditText(binding.etTitle, state.title());

            if (state.date() == null) {
                binding.btnDate.setText("Выберите дату");
            } else {
                final var formattedDate = DateTimeFormatter
                        .ofPattern("HH:mm dd MMM yyyy", Locale.getDefault())
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault())
                        .format(state.date());
                binding.btnDate.setText(formattedDate);
            }

            exerciseListAdapter.submitList(state.selectedExercises());
        });
    }

    private void updateEditText(@NonNull EditText editText, String text) {
        if (!editText.getText().toString().equals(text)) {
            editText.setText(text);
        }
    }

    private void showApproachDialog(@NonNull Exercise exercise) {
        final var editDialog = ApproachAppendDialog.create(exercise);
        editDialog.show(getParentFragmentManager(), "");
    }

    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen(@Nullable Workout existing) {
        return FragmentScreen.Companion.invoke(null, true, f -> {
            final var fragment = new WorkoutAppendFragment();

            if (existing != null) {
                final var args = new Bundle();
                args.putSerializable(existingWorkoutKey, existing);
                fragment.setArguments(args);
            }

            return fragment;
        });
    }

    @Override
    public void addExercise(@NonNull Exercise exercise, int repetitions, int weight) {
        onAction(new WorkoutAppendScreenAction.AppendExercise(exercise, repetitions, weight));
    }

    @Override
    public void onSelected(@NonNull Exercise exercise) {
        showApproachDialog(exercise);
    }
}
