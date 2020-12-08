package com.example.personalhealthmonitor.Home;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.Notification.Alarm;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static com.example.personalhealthmonitor.Utilities.Utility.BATTITORANGE1;
import static com.example.personalhealthmonitor.Utilities.Utility.BATTITORANGE2;
import static com.example.personalhealthmonitor.Utilities.Utility.GLICEMIARANGE1;
import static com.example.personalhealthmonitor.Utilities.Utility.GLICEMIARANGE2;
import static com.example.personalhealthmonitor.Utilities.Utility.NOTIFICATION_ID;
import static com.example.personalhealthmonitor.Utilities.Utility.PRESSIONERANGE1;
import static com.example.personalhealthmonitor.Utilities.Utility.PRESSIONERANGE2;
import static com.example.personalhealthmonitor.Utilities.Utility.SDF;
import static com.example.personalhealthmonitor.Utilities.Utility.TEMPERATURARANGE1;
import static com.example.personalhealthmonitor.Utilities.Utility.TEMPERATURARANGE2;
import static com.example.personalhealthmonitor.Utilities.Utility.reportViewModel;


public class NewReportActivity extends AppCompatActivity {

    private int reportId;
    private EditText ETbattiti, ETpressioneSistolica, ETpressioneDiastolica, ETtemperatura, ETglicemiamax, ETglicemiamin, ETnote;
    private double battiti, pressioneSistolica, pressioneDiastolica, temperatura, glicemiamax, glicemiamin;
    private String nota, ora;
    private Report Report;
    private Date giorno;
    private boolean newReport = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        //RIMUOVE LA NOTIFICA QUANDO LA SI APRE
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        startService(new Intent( this, Alarm.class));

        ETbattiti = findViewById(R.id.ETbattiti);
        ETpressioneSistolica = findViewById(R.id.ETpressioneSistolica);
        ETpressioneDiastolica = findViewById(R.id.ETpressioneDiastolica);
        ETtemperatura = findViewById(R.id.ETtemperatura);
        ETglicemiamax = findViewById(R.id.ETglicemiaMax);
        ETglicemiamin = findViewById(R.id.ETglicemiaMin);
        ETnote = findViewById(R.id.ETnote);
        Button BTNSendReport = findViewById(R.id.Btn_sendReport);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){ //SE ENTRO QU ALLORA STO MODIFICANDO UN REPORT
            this.setTitle(R.string.newreport_title2);
            reportId = bundle.getInt("report_id");
            BTNSendReport.setText(R.string.newreport_BTN1);
            setDataReport();
        }
        else{ //SE ENTRO QUA ALLORA STO AGGIUNGENDO UN NUOVO REPORT
            newReport = true;
            this.setTitle(R.string.newreport_title1);
            BTNSendReport.setText(R.string.newreport_BTN2);
        }

        //QUANDO CLICCO INVIA
        BTNSendReport.setOnClickListener(v -> {
            if(check2input()){
                if(newReport){
                    giorno = Converters.StringToDate(SDF.format(Calendar.getInstance().getTime())); //java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN).format(new Date());
                    ora = java.text.DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ITALIAN).format(new Date());
                    Report report = new Report(0, giorno, ora, battiti,temperatura,pressioneSistolica, pressioneDiastolica, glicemiamax, glicemiamin, nota);
                    reportViewModel.setReport(report);
                    Toast.makeText(getApplication(), "Report salvato", Toast.LENGTH_SHORT).show();
                }
                else {
                    Report = new Report(reportId, giorno, ora, battiti,temperatura,pressioneSistolica, pressioneDiastolica,glicemiamax, glicemiamin, nota);
                    reportViewModel.updateReport(Report);
                    Toast.makeText(getApplication(), "Report modificato", Toast.LENGTH_SHORT).show();
                }
                HomeFragment.setToday();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(this, Alarm.class);
        stopService(i);
    }

    //Scrive sugli edit text i valori già assegnati al report da modificare
    private void setDataReport(){
        //Report report = reportViewModel.getReport(reportId);
        reportViewModel.getReport(reportId).observeForever(report -> {
            giorno = report.getData();
            ora = report.getOra();
            if(report.getBattito() != 0)ETbattiti.setText(String.valueOf(report.getBattito()));
            if(report.getPressione_sistolica() != 0)ETpressioneSistolica.setText(String.valueOf(report.getPressione_sistolica()));
            if(report.getPressione_diastolica() != 0)ETpressioneDiastolica.setText(String.valueOf(report.getPressione_diastolica()));
            if(report.getTemperatura() != 0)ETtemperatura.setText(String.valueOf(report.getTemperatura()));
            if(report.getGlicemiamax() != 0)ETglicemiamax.setText(String.valueOf(report.getGlicemiamax()));
            if(report.getGlicemiamin() != 0)ETglicemiamax.setText(String.valueOf(report.getGlicemiamin()));
            if(report.getBattito() != 0)ETnote.setText(report.getNota());
        });
    }

    //CONTROLLA CHE SIANO STATI INSERITI ALMENO DUE VALORI - SE TRUE ALLORA ALMENO DUE VALORI SONO IMPOSTATI CORRETTAMENTE, ALTRIMENTI FALSE
    private boolean check2input (){
        int count = 0;

        if(!String.valueOf(ETbattiti.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{battiti = Double.parseDouble(String.valueOf(ETbattiti.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error1, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(battiti< BATTITORANGE1 || battiti> BATTITORANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error1, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        if(!String.valueOf(ETtemperatura.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{temperatura = Double.parseDouble(String.valueOf(ETtemperatura.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error2, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(temperatura< TEMPERATURARANGE1 || temperatura> TEMPERATURARANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error2, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        if(!String.valueOf(ETpressioneSistolica.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{pressioneSistolica = Double.parseDouble(String.valueOf(ETpressioneSistolica.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error3, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(pressioneSistolica< PRESSIONERANGE1 || pressioneSistolica> PRESSIONERANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error3, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        if(!String.valueOf(ETpressioneDiastolica.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{pressioneDiastolica = Double.parseDouble(String.valueOf(ETpressioneDiastolica.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error4, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(pressioneDiastolica< PRESSIONERANGE1 || pressioneDiastolica> PRESSIONERANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error4, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        if(!String.valueOf(ETglicemiamax.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{glicemiamax = Double.parseDouble(String.valueOf(ETglicemiamax.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error5, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(glicemiamax< GLICEMIARANGE1 || glicemiamax> GLICEMIARANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error5, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        if(!String.valueOf(ETglicemiamin.getText()).equals("")){ //se non è vuoto
            //controllo che sia un numero valido
            try{glicemiamin = Double.parseDouble(String.valueOf(ETglicemiamin.getText()));}
            catch(NumberFormatException e){ //se non valido
                Toast.makeText(getApplication(), R.string.newreport_error6, Toast.LENGTH_SHORT).show();
                return false;
            }
            if(glicemiamin< GLICEMIARANGE1 || glicemiamin> GLICEMIARANGE2){
                Toast.makeText(getApplication(), R.string.newreport_error6, Toast.LENGTH_SHORT).show();
                return false;
            }
            count++;
        }
        nota = ETnote.getText().toString();
        if(count >= 2) return true;
        else{
            Toast.makeText(getApplication(), "Inserisci almeno due valori", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}