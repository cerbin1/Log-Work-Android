package com.example.bartek.log_work_android;

import android.app.Activity;
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

public class DatePickerActivity extends AppCompatActivity {
    private int year, month, day;
    private static final int Dialog_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        getPickedDate();
    }

    public void getPickedDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(Dialog_ID);
    }

    public Dialog onCreateDialog(int id) {
        if (id == Dialog_ID) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private OnDateSetListener datePickerListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m + 2;
            day = d;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);

            Toast.makeText(DatePickerActivity.this, DateFormat.format("E, d MMMM, yyyy", calendar.getTimeInMillis()), Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Date", calendar.getTimeInMillis());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    };
}
