package Database.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.testdb.MainActivity;
import com.example.testdb.R;
import Database.Report;

public class NewReportActivity extends AppCompatActivity {
    private EditText ReportId, ReportDescrizione;
    private Button BtnInvia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        BtnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int reportid = Integer.parseInt(ReportId.getText().toString());
                String reportdescrizione = ReportDescrizione.getText().toString();

                Report report = new Report();
                report.setId(reportid);
                report.setDescrizione(reportdescrizione);

                MainActivity.db.reportDao().addReport(report);
                Toast.makeText(getApplication(), "Report aggiunto", Toast.LENGTH_SHORT).show();

                ReportId.setText("");
                ReportDescrizione.setText("");
            }
        });
    }
}