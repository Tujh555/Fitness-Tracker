package com.example.fitnesstracker.presentation.basic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.util.function.Consumer;

import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseFragment<S, A, B extends ViewBinding, V extends BaseViewModel<S, A>> extends Fragment {
    private @Nullable B _binding = null;
    private @Nullable V _viewModel = null;
    private @Nullable Disposable stateDisposable = null;

    @Override
    public void onResume() {
        super.onResume();

    }

    @NonNull
    @Override
    public final View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        final B binding = inflateBinding(inflater, container);
        _binding = binding;
        final var viewModel = getViewModel();
        stateDisposable = viewModel
                .observeState()
                .subscribe(this::onStateChanged, e -> {});

        return binding.getRoot();
    }

    @Override
    public final void onDestroyView() {
        if (stateDisposable != null && !stateDisposable.isDisposed()) {
            stateDisposable.dispose();
            stateDisposable = null;
        }
        _binding = null;
        super.onDestroyView();
    }

    protected final @NonNull V getViewModel() {
        if (_viewModel != null) {
            return _viewModel;
        }
        _viewModel = createViewModel();
        return _viewModel;
    }

    protected final void withBinding(@NonNull Consumer<B> callback) {
        if (_binding != null) {
            callback.accept(_binding);
        }
    }

    protected abstract B inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    );

    protected abstract @NonNull V createViewModel();

    protected void onStateChanged(@NonNull S state) {}

    protected final void onAction(@NonNull A action) {
        getViewModel().onAction(action);
    }
}
