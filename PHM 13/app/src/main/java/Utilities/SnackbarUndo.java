package Utilities;

import android.view.View;

import com.example.phm.MainActivity;

import Database.Report;

public class SnackbarUndo implements View.OnClickListener {

    Report reportRimosso;

    @Override
    public void onClick(View v) {

        MainActivity.reportViewModel.setReport(reportRimosso);

    }

    public void reportRimosso(Report report) {
        this.reportRimosso = report;
    }
}
