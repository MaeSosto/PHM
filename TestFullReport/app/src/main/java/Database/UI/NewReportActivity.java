package Database.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.testfullreport.MainActivity;
import com.example.testfullreport.R;

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

        //final String date= java.text.DateFormat.getDateTimeInstance().format(new Date());
        Log.i("MIAO", "SONO IN NEW REPORT");


        //QUANDO CLICCHI SU "SALVA REPORT"
        BTNNuovoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //int reportid = Integer.parseInt(ETId.getText().toString());
                //String reportdescrizione = ETDescrizione.getText().toString();

               // Report report = new Report(0, java.text.DateFormat.getDateTimeInstance().format(new Date()), reportdescrizione);

                //String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
                double battito = Double.parseDouble(ETbattito.getText().toString());
                double temperatura = Double.parseDouble(ETtemperatura.getText().toString());
                double pressione = Double.parseDouble(ETpressione.getText().toString());
                double glicemia = Double.parseDouble(ETglicemia.getText().toString());
                String nota = ETnota.getText().toString();

               // Report report = new Report(0, java.text.DateFormat.getDateTimeInstance().format(new Date()), battito, temperatura, pressione, glicemia, nota);
                Report report = new Report(0, "data1", 1 ,1,1,1, "1");
                //Log.i("MIAO", String.valueOf(report));
                Log.i("MIAO", "HO CREATO IL REPORT");
                MainActivity.reportViewModel.setReport(report);
                Log.i("MIAO", "MIAO");
                Toast.makeText(getApplication(), "Report salvato", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}