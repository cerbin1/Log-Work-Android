package com.example.bartek.log_work_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import utils.PatternChecker;

import static android.widget.Toast.LENGTH_SHORT;
import static utils.Formatter.getToastFormattedAsError;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int DATE_PICKER_REQUEST_CODE = 10;
    private final Context CONTEXT = MainActivity.this;

    private DatabaseHelper database;

    private double hoursWorked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(CONTEXT, SecondActivity.class);
        startActivity(intent);
    }

    public void submitWorkedHours(View view) {
        String input = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
        if (PatternChecker.matches(input)) {
            hoursWorked = Double.parseDouble(input);

            if (isCustomDateSetChecked()) {
                startDatePickerActivity();
            } else {
                boolean isInserted = database.insert(getCurrentDate(), hoursWorked);
                Toast.makeText(MainActivity.this, isInserted ? "Data inserted" : "Data not inserted", Toast.LENGTH_SHORT).show();
            }
        } else {
            getToastFormattedAsError(CONTEXT, "Wrong input!", LENGTH_SHORT).show();
        }
    }

    private boolean isCustomDateSetChecked() {
        return ((CheckBox) findViewById(R.id.setDate)).isChecked();
    }

    private void startDatePickerActivity() {
        Intent intent = new Intent(CONTEXT, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long dateInMillis = data.getLongExtra("Date", 0);
                database.insert(dateInMillis, hoursWorked);
            }
        }
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
