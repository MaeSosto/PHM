package Database.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.testfullreport.MainActivity;
import com.example.testfullreport.R;

import Database.Report;
import Database.ReportViewModel;

public class EditReportActivity extends AppCompatActivity {

    //VARIABILI
    Bundle bundle;
    int reportId;
    LiveData<Report> report;
    EditText ETDescrizione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        ETDescrizione = findViewById(R.id.ETDescrizione_EDIT);

        bundle = getIntent().getExtras();
        if(bundle!= null){
            reportId = bundle.getInt("report_id");
            Log.i("ID: ", Integer.toString(reportId));
        }
        else{
            Log.i("Errore", "Non arriva il bundle");
        }

        MainActivity.reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        report = MainActivity.reportViewModel.getReport(reportId);

        report.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(@Nullable Report report) {
                ETDescrizione.setText(report.getNota());
            }
        });
    }

    public void updateReport(View view){
        String updateReport = ETDescrizione.getText().toString();
        //Report report = new Report(, updateReport);
        //MainActivity.reportViewModel.updateReport(report);
        Toast.makeText(getApplication(), "Report modificato", Toast.LENGTH_SHORT).show();

        finish();
    }


}

class SnackbarUndo implements View.OnClickListener {

    Report reportRimosso;

    @Override
    public void onClick(View v) {

        MainActivity.reportViewModel.setReport(reportRimosso);

    }

    public void reportRimosso(Report report) {
        this.reportRimosso = report;
    }
}