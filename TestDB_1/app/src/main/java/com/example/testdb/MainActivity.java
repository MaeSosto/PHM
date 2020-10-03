package com.example.testdb;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.UUID;
import Database.Report;
import Database.DB;
import Database.NewReportActivity;
import Database.ReportViewModel;


public class MainActivity extends AppCompatActivity {

    private static final int NEW_REPORT_ACTIVITY_REQUEST_CODE = 1;
    DB db = Room.databaseBuilder(getApplicationContext(),
            DB.class, "DB").build();

    private ReportViewModel reportViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, NewReportActivity.class);
                startActivityForResult(intent, NEW_REPORT_ACTIVITY_REQUEST_CODE);

                 */
            }
        });

        //reportViewModel = ViewModelProvider.of(this).get(ReportViewModel.class);
    }

    /*
    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Controllo il result code, se è okay allora lo inserisco nel db altrimenti no
        if(requestCode == NEW_REPORT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            final String report_id = UUID.randomUUID().toString();
            Report report = new Report(report_id, data.getStringExtra(
                    NewReportActivity.REPORT_ADDED));
            reportViewModel.insert(report);


            Toast.makeText(getApplicationContext(),
                    R.string.salva,
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    R.string.non_salvato,
                    Toast.LENGTH_LONG).show();
        }
    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}