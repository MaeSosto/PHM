package com.example.healthcaremonitor_v0.UI.diario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthcaremonitor_v0.R;

public class DiarioFragment extends Fragment {

    public DiarioFragment() {

    }

    public static Fragment newInstance(String s, String s1) {
        DiarioFragment fragment = new DiarioFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_diario, container, false);
    }
}