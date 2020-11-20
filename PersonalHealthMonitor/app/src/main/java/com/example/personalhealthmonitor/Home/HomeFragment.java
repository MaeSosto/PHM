package com.example.personalhealthmonitor.Home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    public static ReportViewModel reportViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //FAB
        FloatingActionButton fab = root.findViewById(R.id.FabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              FAB(fab);
            }
        });

        return root;
    }

    private void FAB(FloatingActionButton fab){
        Intent intent = new Intent(getContext(), NewReportActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(fab,"activity_trans");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(intent,options.toBundle());
    }
}