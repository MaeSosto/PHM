package Database.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        ReportId = findViewById(R.id.ETid);
        ReportDescrizione = findViewById(R.id.ETdescrizione);
        BtnInvia = findViewById(R.id.Btn_newReport);

        BtnInvia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i("Log", "Bottone");
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