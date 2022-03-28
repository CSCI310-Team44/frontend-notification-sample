package com.example.project2notificationsample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.project2notificationsample.databinding.FragmentFirstBinding;

import java.io.IOException;
import java.net.URL;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        binding.textviewFirst.setText("Dummy");

        Notifier notifier = new Notifier(
                1,
                0,
                str -> {
                    // Android UI must go on UI thread
                    // https://stackoverflow.com/questions/7860384/
                    // android-how-to-runonuithread-in-other-class
                    new Handler(Looper.getMainLooper()).post(
                            () -> binding.textviewFirst.setText(str)
                    );
                }
        );
        notifier.start();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}