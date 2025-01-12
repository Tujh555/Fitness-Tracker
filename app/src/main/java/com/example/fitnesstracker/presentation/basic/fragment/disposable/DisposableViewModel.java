package com.example.fitnesstracker.presentation.basic.fragment.disposable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.fitnesstracker.presentation.basic.fragment.BaseViewModel;

import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public abstract class DisposableViewModel<S, A> extends ViewModel implements BaseViewModel<S, A> {
    protected final @NonNull CompositeDisposable disposables = new CompositeDisposable();
    protected final @NonNull BehaviorSubject<S> stateSubject;

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

    protected final void onSubscribe(@NonNull SingleFactory factory) {
        factory.create().subscribe((v) -> {}, (e) -> {}, disposables);
    }

    protected final void onSubscribe(@NonNull FlowableFactory factory) {
        factory.create().subscribe((v) -> {}, (t) -> {}, () -> {}, disposables);
    }

    @Override
    protected final void onCleared() {
        super.onCleared();

        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    protected final void updateState(@NonNull Function<S, S> block) {
        stateSubject.onNext(block.apply(stateSubject.getValue()));
    }

    protected final <R> R withState(@NonNull Function<S, R> block) {
        return block.apply(stateSubject.getValue());
    }
}
