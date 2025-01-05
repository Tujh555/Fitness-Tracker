package com.example.fitnesstracker.presentation.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.domain.User;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import org.jetbrains.annotations.Contract;

// TODO
public class MainPageFragment extends Fragment {
    @NonNull
    @Contract("_ -> new")
    public static FragmentScreen getScreen(User user) {
        return FragmentScreen.Companion.invoke(
                null,
                true,
                (f) -> new MainPageFragment()
        );
    }
}
