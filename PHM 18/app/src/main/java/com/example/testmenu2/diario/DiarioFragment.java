package com.example.testmenu2.diario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testmenu2.R;

public class DiarioFragment extends Fragment {

    private DiarioViewModel diarioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diarioViewModel =
                new ViewModelProvider(this).get(DiarioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diario, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        diarioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}