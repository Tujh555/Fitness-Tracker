package com.example.fitnesstracker.presentation.exercise.edit;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fitnesstracker.platform.PutExerciseService;
import com.example.fitnesstracker.presentation.basic.fragment.disposable.DisposableViewModel;
import com.example.fitnesstracker.presentation.exercise.edit.action.ExerciseAppendScreenAction;
import com.example.fitnesstracker.presentation.exercise.edit.state.ExerciseAppendScreenState;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class ExerciseAppendViewModel extends DisposableViewModel<ExerciseAppendScreenState, ExerciseAppendScreenAction> {
    private final @NonNull Router router;
    private final @NonNull Context context;

    @Inject
    public ExerciseAppendViewModel(
            @NonNull Router router,
            @NonNull @ApplicationContext Context context
    ) {
        super(new ExerciseAppendScreenState(null, null, ""));
        this.router = router;
        this.context = context;
    }

    @Override
    public void onAction(@NonNull ExerciseAppendScreenAction action) {
        if (action instanceof ExerciseAppendScreenAction.Cancel) {
            router.exit();
        } else if (action instanceof ExerciseAppendScreenAction.Save) {
            save();
        } else if (action instanceof ExerciseAppendScreenAction.TitleInput titleInput) {
            titleInput(titleInput.text());
        } else if (action instanceof ExerciseAppendScreenAction.UriReceived uriReceived) {
            onUriReceived(uriReceived.uri());
        } else if (action instanceof ExerciseAppendScreenAction.IdReceived idReceived) {
            onIdReceived(idReceived.id());
        }
    }

    private void onIdReceived(@NonNull String id) {
        updateState(state -> new ExerciseAppendScreenState(id, state.uri(), state.title()));
    }

    private void onUriReceived(@NonNull Uri uri) {
        updateState(state -> new ExerciseAppendScreenState(state.existingId(), uri, state.title()));
    }

    private void titleInput(@NonNull String titleText) {
        updateState(state -> new ExerciseAppendScreenState(state.existingId(), state.uri(), titleText));
    }

    private void save() {
        final var state = stateSubject.getValue();

        if (state.existingId() != null) {
            PutExerciseService.edit(context, state.existingId(), state.uri(), state.title());
        } else {
            PutExerciseService.create(context, state.uri(), state.title());
        }

        router.exit();
    }
}
