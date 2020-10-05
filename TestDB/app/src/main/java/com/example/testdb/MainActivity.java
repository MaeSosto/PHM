package com.example.testdb;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import Database.DB;
import Database.ui.NewReportActivity;

public class MainActivity extends AppCompatActivity {

    //Variables
    public static FragmentManager fragmentManager;
    public static DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inizializzo il DB
        //db = Room.databaseBuilder(getApplicationContext(), DB.class, "reportdb").build();
        db = Room.databaseBuilder(getApplicationContext(), DB.class, "reportdb").allowMainThreadQueries().build();


        //Fragment container
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container)!= null){
            if(savedInstanceState!= null){ return;}
            fragmentManager.beginTransaction().add(R.id.fragment_container, new RerportFragmentList()).commit();

        }

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                 */
                Intent intent = new Intent(getApplicationContext(), NewReportActivity.class);
                startActivity(intent);
            }
        });
    }

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