package Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testdb.R;

public class NewReportActivity extends AppCompatActivity {
    public static final String REPORT_ADDED = "new_report";

    private EditText etNewReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        etNewReport = findViewById(R.id.etNewReport);

        Button button = findViewById(R.id.bAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inizializzo l'intent con il risultato
                Intent resultIntent = new Intent();

                //Se è vuoto allora il risultato è cancelled
                if(TextUtils.isEmpty(etNewReport.getText())){
                    setResult(RESULT_CANCELED, resultIntent);
                }
                //Altrimenti il risultato è okay e ha il valore report contenuto nell'edittext
                else {
                    String report = etNewReport.getText().toString();
                    resultIntent.putExtra(REPORT_ADDED, report);
                    setResult(RESULT_OK, resultIntent);
                }

                finish();
            }
        });


    }
}
