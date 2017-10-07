package com.example.bartek.log_work_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Calendar;

import utils.RegexPatternValidator;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static utils.Formatter.getToastFormattedAsError;

public class MainActivity extends AppCompatActivity {
    private static final int DATE_PICKER_REQUEST_CODE = 10;

    private DatabaseHelper database;

    private double hoursWorked;
    private CheckBox customDateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        customDateCheckBox = ((CheckBox) findViewById(R.id.setDate));
    }

    public void startDisplayHistoryActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void submitWorkedHours(View view) {
        String input = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
        if (RegexPatternValidator.isValid(input)) {
            hoursWorked = Double.parseDouble(input);

            if (isCustomDateSetChecked()) {
                startDatePickerActivity();
            } else {
                boolean isInserted = database.insert(getCurrentDate(), hoursWorked);
                displayToast(isInserted);
            }
        } else {
            getToastFormattedAsError(this, "Wrong input!", LENGTH_SHORT).show();
        }
    }

    private boolean isCustomDateSetChecked() {
        return customDateCheckBox.isChecked();
    }

    private void startDatePickerActivity() {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long dateInMillis = data.getLongExtra("Date", 0);
                boolean isInserted = database.insert(dateInMillis, hoursWorked);
                displayToast(isInserted);
            }
        }
    }

    private void displayToast(boolean isInserted) {
        makeText(this, isInserted ? "Saved" : "Data not saved", LENGTH_SHORT).show();
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
