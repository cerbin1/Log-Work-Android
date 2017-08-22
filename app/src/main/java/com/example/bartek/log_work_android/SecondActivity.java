package com.example.bartek.log_work_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        TextView workedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        workedHoursTextView.setText(getIntent().getExtras().getString("string"));
    }
}
