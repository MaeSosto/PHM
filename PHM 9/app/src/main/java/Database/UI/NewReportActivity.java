package Database.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Database.Report;

import com.example.phm.MainActivity;
import com.example.phm.R;

import java.util.Date;

public class NewReportActivity extends AppCompatActivity {

    //VARIABILI
    private EditText ETbattiti, ETpressione, ETtemperatura, ETglicemia, ETnote;
    private Button BTNNuovoReport;
    private double battiti, pressione, temperatura, glicemia;
    private String nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        ETbattiti = findViewById(R.id.ETbattiti);
        ETpressione = findViewById(R.id.ETpressione);
        ETtemperatura = findViewById(R.id.ETtemperatura);
        ETglicemia = findViewById(R.id.ETglicemia);
        ETnote = findViewById(R.id.ETnote);
        BTNNuovoReport = findViewById(R.id.Btn_newReport);

        //QUANDO CLICCHI SU "SALVA REPORT"
        BTNNuovoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check2input() < 2) Toast.makeText(getApplication(), "Inserisci almeno due valori", Toast.LENGTH_SHORT).show();
                else{
                    Report report = new Report(0, java.text.DateFormat.getDateTimeInstance().format(new Date()),battiti,temperatura,pressione,glicemia, nota);

                    MainActivity.reportViewModel.setReport(report);
                    Toast.makeText(getApplication(), "Report salvato", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    //CONTROLLA CHE SIANO STATI INSERITI ALMENO DUE VALORI
    private int check2input (){
        int count = 0;

        if(!ETbattiti.getText().toString().matches("")){
            battiti = Double.parseDouble(ETbattiti.getText().toString());
            count++;
        }
        if(!ETpressione.getText().toString().matches("")){
            pressione = Double.parseDouble(ETpressione.getText().toString());
            count++;
        }
        if(!ETtemperatura.getText().toString().matches("")){
            temperatura = Double.parseDouble(ETtemperatura.getText().toString());
            count++;
        }
        if(!ETglicemia.getText().toString().matches("")){
            glicemia = Double.parseDouble(ETglicemia.getText().toString());
            count++;
        }
        if(!ETnote.getText().toString().matches("")){
            nota = ETnote.getText().toString();
            count++;
        }
        return count;
    }
}