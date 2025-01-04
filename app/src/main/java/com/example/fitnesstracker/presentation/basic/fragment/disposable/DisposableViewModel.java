package com.example.fitnesstracker.presentation.basic.fragment.disposable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.example.fitnesstracker.presentation.basic.fragment.BaseViewModel;

import java.util.function.Consumer;
import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public abstract class DisposableViewModel<S, A> extends ViewModel implements BaseViewModel<S, A> {
    private final @NonNull CompositeDisposable disposables = new CompositeDisposable();
    protected final @NonNull BehaviorSubject<S> stateSubject;
    private @Nullable FragmentManager fragmentManager;

    public DisposableViewModel(S initialState) {
        stateSubject = BehaviorSubject.createDefault(initialState);
    }

    @NonNull
    @Override
    public final Flowable<S> observeState() {
        return stateSubject
                .toFlowable(BackpressureStrategy.LATEST)
                .distinct()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setFragmentManager(@Nullable FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    protected final void onSubscribe(@NonNull SingleFactory factory) {
        final var disposable = factory
                .create()
                .subscribe((v) -> {}, (e) -> {});

        disposables.add(disposable);
    }

    @Override
    protected final void onCleared() {
        super.onCleared();

        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    protected final void updateState(Function<S, S> block) {
        stateSubject.onNext(block.apply(stateSubject.getValue()));
    }

    protected final <R> R withState(Function<S, R> block) {
        return block.apply(stateSubject.getValue());
    }

    protected final void navigate(Consumer<FragmentManager> block) {
        if (fragmentManager != null) {
            block.accept(fragmentManager);
        }
    }
}
