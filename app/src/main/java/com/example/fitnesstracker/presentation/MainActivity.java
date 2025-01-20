package com.example.fitnesstracker.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.databinding.ActivityMainBinding;
import com.example.fitnesstracker.domain.auth.AuthRepository;
import com.example.fitnesstracker.domain.profile.ProfileRepository;
import com.example.fitnesstracker.presentation.auth.AuthFragment;
import com.example.fitnesstracker.presentation.exercise.list.ExerciseListFragment;
import com.example.fitnesstracker.presentation.main.MainPageFragment;
import com.example.fitnesstracker.presentation.profile.view.ProfileViewFragment;
import com.example.fitnesstracker.presentation.workout.create.WorkoutAppendFragment;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Router;
import com.github.terrakok.cicerone.androidx.AppNavigator;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    NavigatorHolder navigatorHolder;
    @Inject
    Router router;
    @Inject
    ProfileRepository repository;
    @Inject
    AuthRepository authRepository;
    private Navigator navigator;
    private final @NonNull CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navigator = new AnimatedAppNavigator(this, R.id.main_container);
        binding.bottomNav.setVisibility(View.GONE);

        repository.observe().subscribe(
                (user) -> binding.bottomNav.setVisibility(View.VISIBLE),
                (e) -> {},
                () -> {},
                disposables
        );

        if (savedInstanceState == null) {
            authRepository
                    .getExisting()
                    .timeout(200, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            user -> router.newRootScreen(MainPageFragment.getScreen()),
                            e -> router.newRootScreen(AuthFragment.getScreen()),
                            disposables
                    );
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                router.newRootScreen(MainPageFragment.getScreen());
                return true;
            }

            if (item.getItemId() == R.id.menu_workouts) {
                router.newRootScreen(WorkoutAppendFragment.getScreen(null));
                return true;
            }

            if (item.getItemId() == R.id.menu_profile) {
                router.newRootScreen(ProfileViewFragment.getScreen());
                return true;
            }

            if (item.getItemId() == R.id.menu_exercise) {
                router.newRootScreen(ExerciseListFragment.getScreen());
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onDestroy();
    }

    private static class AnimatedAppNavigator extends AppNavigator {
        public AnimatedAppNavigator(@NonNull FragmentActivity activity, int containerId) {
            super(activity, containerId);
        }

        @Override
        protected void setupFragmentTransaction(
                @NonNull FragmentScreen screen,
                @NonNull FragmentTransaction fragmentTransaction,
                @Nullable Fragment currentFragment,
                @NonNull Fragment nextFragment
        ) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
            );
        }
    }
}