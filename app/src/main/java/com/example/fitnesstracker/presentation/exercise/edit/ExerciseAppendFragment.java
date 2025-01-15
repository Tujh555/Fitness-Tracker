package com.example.fitnesstracker.presentation.exercise.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.PickVisualMediaRequestKt;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.FragmentExerciseEditBinding;
import com.example.fitnesstracker.domain.workout.models.Exercise;
import com.example.fitnesstracker.presentation.SimpleTextWatcher;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.exercise.edit.action.ExerciseAppendScreenAction;
import com.example.fitnesstracker.presentation.exercise.edit.state.ExerciseAppendScreenState;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

public class ExerciseAppendFragment extends BaseFragment<ExerciseAppendScreenState, ExerciseAppendScreenAction, FragmentExerciseEditBinding, ExerciseAppendViewModel> {
    private static final String existingExerciseKey = "exercise";
    private ActivityResultLauncher<PickVisualMediaRequest> pickImage;

    @Override
    protected FragmentExerciseEditBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentExerciseEditBinding.inflate(inflater, container, false);

        binding.btnCancel.setOnClickListener(v -> onAction(new ExerciseAppendScreenAction.Cancel()));
        binding.btnSave.setOnClickListener(v -> onAction(new ExerciseAppendScreenAction.Save()));

        final var arguments = getArguments();

        if (arguments != null) {
            final var existingExercise = (Exercise) arguments.getSerializable(existingExerciseKey);

            if (existingExercise != null) {
                onAction(new ExerciseAppendScreenAction.IdReceived(existingExercise.id()));
                binding.etTitle.setText(existingExercise.title());

                Glide.with(this)
                        .load(existingExercise.describingPhoto())
                        .placeholder(R.drawable.ic_workout)
                        .centerCrop()
                        .into(binding.ivDescribingPhoto);
            }
        }

        pickImage = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> onAction(new ExerciseAppendScreenAction.UriReceived(uri))
        );
        binding.ivDescribingPhoto.setOnClickListener((v) -> {
            final var request = PickVisualMediaRequestKt.PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE
            );
            pickImage.launch(request);
        });

        binding.etTitle.addTextChangedListener(
                SimpleTextWatcher.create(text ->
                        onAction(new ExerciseAppendScreenAction.TitleInput(text))
                )
        );

        return binding;
    }

    @Override
    protected void onStateChanged(@NonNull ExerciseAppendScreenState state) {
        super.onStateChanged(state);
        withBinding(binding -> {
            updateEditText(binding.etTitle, state.title());

            if (state.uri() != null) {
                Glide.with(this)
                        .load(state.uri())
                        .centerCrop()
                        .into(binding.ivDescribingPhoto);
            }
        });
    }

    private void updateEditText(@NonNull EditText editText, String text) {
        if (!editText.getText().toString().equals(text)) {
            editText.setText(text);
        }
    }

    @NonNull
    @Override
    protected ExerciseAppendViewModel createViewModel() {
        return new ViewModelProvider(this).get(ExerciseAppendViewModel.class);
    }

    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen(@Nullable Exercise exercise) {
        return FragmentScreen.Companion.invoke(null, true, f -> {
            final var fragment = new ExerciseAppendFragment();

            if (exercise != null) {
                final var args = new Bundle();
                args.putSerializable(existingExerciseKey, exercise);
                fragment.setArguments(args);
            }

            return fragment;
        });
    }
}
