package com.example.fitnesstracker.presentation.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesstracker.databinding.FragmentMainBinding;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.main.action.MainPageScreenAction;
import com.example.fitnesstracker.presentation.main.components.WorkoutAdapter;
import com.example.fitnesstracker.presentation.main.state.MainFragmentState;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

public class MainPageFragment extends BaseFragment<MainFragmentState, MainPageScreenAction, FragmentMainBinding, MainViewModel> {
    private WorkoutAdapter workoutAdapter;

    @Override
    protected FragmentMainBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentMainBinding.inflate(inflater, container, false);

        workoutAdapter = new WorkoutAdapter();
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
            workoutAdapter.submitData(getLifecycle(), workouts);
        }
    }

    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen() {
        return FragmentScreen.Companion.invoke(
                null,
                true,
                (f) -> new MainPageFragment()
        );
    }
}
