package com.example.fitnesstracker.presentation.workout.create.dialogs;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Consumer;

public class DatePickerFactory {
    @NonNull
    public static MaterialDatePicker<Long> date(@NonNull Consumer<Calendar> apply) {
        final var datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Выберите дату")
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            final var calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(selection);
            apply.accept(calendar);
        });

        return datePicker;
    }

    @NonNull
    public static MaterialTimePicker time(@NonNull Calendar calendar, @NonNull Consumer<Instant> apply) {
        final var timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Выберите время")
                .build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            final var hour = timePicker.getHour();
            final var minute = timePicker.getMinute();

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            apply.accept(calendar.toInstant());
        });

        return timePicker;
    }
}
