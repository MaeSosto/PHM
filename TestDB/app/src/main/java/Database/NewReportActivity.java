package Database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testdb_4.R;

import static com.example.testdb_4.MainActivity.diario;

public class NewReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_report);
        Button send = findViewById(R.id.button_send);
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView descrizione = findViewById(R.id.editTextNumber);
                String Sdescrizione = descrizione.getText().toString();

                Report report = new Report();
                report.setDescrizione(Sdescrizione);

                diario.reportDao().setReport(report);

                /*CharSequence text = "Report added";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();

                 */
            }
        });
    }
}