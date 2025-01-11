package com.example.fitnesstracker.presentation.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.databinding.FragmentProfileBinding;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.profile.action.ProfileScreenAction;
import com.example.fitnesstracker.presentation.profile.state.ProfileScreenState;

public class ProfileFragment extends BaseFragment<ProfileScreenState, ProfileScreenAction, FragmentProfileBinding, ProfileViewModel> {
    @Override
    protected FragmentProfileBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentProfileBinding.inflate(inflater, container, false);

        

        return binding;
    }

    @NonNull
    @Override
    protected ProfileViewModel createViewModel() {
        return new ViewModelProvider(this).get(ProfileViewModel.class);
    }
}
