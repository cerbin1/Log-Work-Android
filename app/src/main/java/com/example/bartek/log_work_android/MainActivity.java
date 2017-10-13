package com.example.bartek.log_work_android;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import utils.RegexPatternValidator;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static utils.Formatter.getToastFormattedAsError;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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
        if (RegexPatternValidator.isValid(input) && !input.equals("0")) {
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
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void displayToast(boolean isInserted) {
        makeText(this, isInserted ? "Saved" : "Data not saved", LENGTH_SHORT).show();
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(DAY_OF_MONTH, i2);
        calendar.set(MONTH, i1);
        calendar.set(YEAR, i);

        long dateInMillis = calendar.getTimeInMillis();
        boolean isInserted = database.insert(dateInMillis, hoursWorked);
        displayToast(isInserted);
    }
}
