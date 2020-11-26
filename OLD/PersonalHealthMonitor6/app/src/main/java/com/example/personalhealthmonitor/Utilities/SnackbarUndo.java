package com.example.personalhealthmonitor.Utilities;

import android.view.View;
import com.example.personalhealthmonitor.Database.Report;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class SnackbarUndo implements View.OnClickListener {

    Report reportRimosso;

    @Override
    public void onClick(View v) {
        reportViewModel.setReport(reportRimosso);
    }

    public void reportRimosso(Report report) {
        this.reportRimosso = report;
    }
}
