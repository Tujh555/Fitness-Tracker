package com.example.fitnesstracker.presentation.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesstracker.databinding.FragmentMainBinding;
import com.example.fitnesstracker.domain.workout.WorkoutSummary;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.main.action.MainPageScreenAction;
import com.example.fitnesstracker.presentation.main.components.WorkoutAdapter;
import com.example.fitnesstracker.presentation.main.state.MainFragmentState;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainPageFragment extends BaseFragment<MainFragmentState, MainPageScreenAction, FragmentMainBinding, MainViewModel> {
    private WorkoutAdapter workoutAdapter;

    @Override
    protected FragmentMainBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentMainBinding.inflate(inflater, container, false);

        workoutAdapter = new WorkoutAdapter(workout -> onAction(new MainPageScreenAction.Edit(workout)));
        final var layoutManager = new LinearLayoutManager(
                binding.getRoot().getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        binding.rvWorkouts.setLayoutManager(layoutManager);
        binding.rvWorkouts.setAdapter(workoutAdapter);

        return binding;
    }

    @NonNull
    @Override
    protected MainViewModel createViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void onStateChanged(@NonNull MainFragmentState state) {
        super.onStateChanged(state);
        final var workouts = state.workouts();

        if (workouts != null) {
            workoutAdapter.submitList(workouts);
        }
        setupChart(state.summaries());
    }

    private void setupChart(@NonNull List<WorkoutSummary> summaries) {
        final var entries = new ArrayList<BarEntry>(summaries.size());
        final var labels = new ArrayList<String>(summaries.size());

        for (int i = 0; i < summaries.size(); i++) {
            final var summary = summaries.get(i);
            entries.add(new BarEntry(i, summary.tonnage()));
            labels.add(summary.date());
        }

        final var dataSet = new BarDataSet(entries, "Тоннаж тренировок");
        dataSet.setColor(Color.GREEN);
        final var barData = new BarData(dataSet);

        withBinding(binding -> {
            binding.barChart.setData(barData);
            final var xAxis = binding.barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

            final var yAxis = binding.barChart.getAxisLeft();
            yAxis.setDrawGridLines(false);
            yAxis.setAxisMinimum(0f);

            binding.barChart.getAxisRight().setEnabled(false);

            final var description = new Description();
            description.setText("Прогресс тренировок");
            binding.barChart.setDescription(description);
            binding.barChart.animateY(1000);

            binding.barChart.invalidate();
        });
    }

    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen() {
        return FragmentScreen.Companion.invoke(null, true, (f) -> new MainPageFragment());
    }
}
