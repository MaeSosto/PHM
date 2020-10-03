package Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testdb_2.R;

public class NewReportActivity extends AppCompatActivity {

    public static final String REPORT_ADDED = "new_report";
    private EditText ETbattiti;
    private Button invia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_report);


        invia = findViewById(R.id.bottone_aggiungi_report);
        ETbattiti = findViewById(R.id.ETbattito_cardiaco);

        //Quando clicco su invia
        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inizializzo l'intent con il risultato
                Intent resultIntent = new Intent();

                //Se è vuoto allora il risultato è cancelled
                if(TextUtils.isEmpty(ETbattiti.getText())){
                    setResult(RESULT_CANCELED, resultIntent);
                }
                //Altrimenti aggiungo il report
                else {
                    String report = ETbattiti.getText().toString();
                    resultIntent.putExtra(REPORT_ADDED, report);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
            }
        });

    }



}
