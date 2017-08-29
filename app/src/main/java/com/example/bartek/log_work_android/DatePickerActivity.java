package com.example.bartek.log_work_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.DatePickerDialog.OnDateSetListener;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class DatePickerActivity extends AppCompatActivity {
    private int year, month, day;
    private static final int DATE_PICKER_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        initializeDatePickerDialog();
    }

    public void initializeDatePickerDialog() {
        setCurrentDateToDialog();
        showDialog(DATE_PICKER_DIALOG_ID);
    }

    private void setCurrentDateToDialog() {
        final Calendar calendar = getInstance();
        year = calendar.get(YEAR);
        month = calendar.get(MONTH);
        day = calendar.get(DAY_OF_MONTH);
    }

    public Dialog onCreateDialog(int id) {
        if (id == DATE_PICKER_DIALOG_ID) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private OnDateSetListener datePickerListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m;
            day = d;

            final Calendar calendar = getInstance();
            calendar.set(DAY_OF_MONTH, day);
            calendar.set(MONTH, month);
            calendar.set(YEAR, year);

            Toast.makeText(DatePickerActivity.this, DateFormat.format("E, d MMMM, yyyy", calendar.getTimeInMillis()), Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Date", calendar.getTimeInMillis());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };
}
