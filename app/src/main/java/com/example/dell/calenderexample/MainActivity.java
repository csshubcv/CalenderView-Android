package com.example.dell.calenderexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.calenderexample.fragments.CalendarFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.content_panel,new CalendarFragment()).commit();
    }

}
