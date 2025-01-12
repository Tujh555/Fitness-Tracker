package com.example.fitnesstracker.presentation.profile.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.FragmentViewProfileBinding;
import com.example.fitnesstracker.domain.User;
import com.example.fitnesstracker.presentation.basic.fragment.BaseFragment;
import com.example.fitnesstracker.presentation.profile.view.action.ProfileViewAction;

public class ProfileViewFragment extends BaseFragment<User, ProfileViewAction, FragmentViewProfileBinding, ProfileViewModel> {
    @Override
    protected FragmentViewProfileBinding inflateBinding(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container
    ) {
        final var binding = FragmentViewProfileBinding.inflate(inflater, container, false);
        binding.btnEdit.setOnClickListener(v -> onAction(new ProfileViewAction.Edit()));
        binding.btnExit.setOnClickListener(v -> onAction(new ProfileViewAction.Exit()));
        return binding;
    }

    @Override
    protected void onStateChanged(@NonNull User state) {
        super.onStateChanged(state);
        withBinding(binding -> {
            if (state.avatar() != null) {
                Glide.with(this)
                        .load(state.avatar())
                        .centerCrop()
                        .placeholder(R.drawable.ic_user)
                        .into(binding.ivAvatar);
            }

            if (state.age() != null) {
                binding.tvAge.setText(state.age().toString());
            }

            binding.tvLogin.setText(state.login());
            binding.tvName.setText(state.name());
            binding.tvTarget.setText(state.target());
        });
    }

    @NonNull
    @Override
    protected ProfileViewModel createViewModel() {
        return new ViewModelProvider(this).get(ProfileViewModel.class);
    }
}
