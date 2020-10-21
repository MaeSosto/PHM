package Database.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfullreport1.MainActivity;
import com.example.testfullreport1.R;
import com.google.android.material.button.MaterialButton;

import java.util.Date;

import Database.Report;

public class NewReportActivity extends AppCompatActivity {

    //VARIABILI
    private EditText ETbattito, ETtemperatura, ETpressione, ETglicemia, ETnota;
    private Button BTNNuovoReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        ETbattito = findViewById(R.id.ETBattiti);
        ETtemperatura = findViewById(R.id.ETTemperatura);
        ETpressione = findViewById(R.id.ETPressione);
        ETglicemia = findViewById(R.id.ETGlicemia);
        ETnota = findViewById(R.id.ETNota);
        BTNNuovoReport = findViewById(R.id.Btn_newReport);

        BTNNuovoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
                double battito = Double.parseDouble(ETbattito.getText().toString());
                double temperatura = Double.parseDouble(ETtemperatura.getText().toString());
                double pressione = Double.parseDouble(ETpressione.getText().toString());
                double glicemia = Double.parseDouble(ETglicemia.getText().toString());
                String nota = ETnota.getText().toString();

                 */

                Report report = new Report(0, java.text.DateFormat.getDateTimeInstance().format(new Date()),1,1,1,1,"1");

                MainActivity.reportViewModel.setReport(report);
                Toast.makeText(getApplicationContext(), "Report salvato", Toast.LENGTH_SHORT);
            }
        });
    }
}