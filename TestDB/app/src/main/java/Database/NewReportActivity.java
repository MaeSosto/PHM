package Database;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.testdb_6.R;


public class NewReportActivity extends AppCompatActivity {

    public static Diario myAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        // myAppDatabase.reportDao().insert();



    }
}