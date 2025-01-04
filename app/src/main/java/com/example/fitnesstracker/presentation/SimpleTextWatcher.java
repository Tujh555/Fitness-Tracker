package com.example.fitnesstracker.presentation;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

public class SimpleTextWatcher {
    @NonNull
    @Contract("_ -> new")
    public static TextWatcher create(@NonNull Consumer<String> supplier) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                supplier.accept(editable.toString());
            }
        };
    }
}
