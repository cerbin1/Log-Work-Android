package com.example.bartek.log_work_android;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.AlertDialog.Builder;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Double.parseDouble;
import static utils.Formatter.formatDouble;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final double SALARY_PER_HOUR = 9.0;
    private final SecondActivity CONTEXT = SecondActivity.this;

    private TextView sumOfWorkedHoursTextView;
    private TextView workHistoryTextView;
    private double sumOfWorkedHours;

    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        database = new DatabaseHelper(this);

        String sumOfWorkedHoursString = getSumOfWorkedHoursFromExtras();
        sumOfWorkedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        sumOfWorkedHoursTextView.setText(sumOfWorkedHoursString);
        sumOfWorkedHours = parseDouble(sumOfWorkedHoursString);
        workHistoryTextView = (TextView) findViewById(R.id.workHistory);

        workHistoryTextView.setText(getWorkHistoryFromDatabase());
    }

    private String getWorkHistoryFromDatabase() {
        Cursor data = database.getWorkHistory();
        if (data.getCount() == 0) {
            Toast.makeText(this, "No history work", LENGTH_LONG).show();
            return "";
        }

        StringBuilder builder = new StringBuilder();
        while (data.moveToNext()) {
            builder
                    .append(data.getString(0)).append(" ")
                    .append(data.getString(1)).append(" ")
                    .append(data.getString(2)).append("\n");
        }
        return builder.toString();
    }

    private String getSumOfWorkedHoursFromExtras() {
        return getIntent().getExtras().getString("sumOfWorkedHours");
    }

    public void deleteSumOfWorkedHours(View view) {
        Builder builder = new Builder(this);
        builder.setMessage("Are you sure you want to delete sum of worked hours?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makeText(CONTEXT, "Deleted", LENGTH_SHORT).show();
                        try {
                            clearFiles();
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "FileNotFoundException " + e.getMessage());
                        } catch (IOException e) {
                            Log.e(TAG, "IOException " + e.getMessage());
                        }
                        resetTextViewsAndFields();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void resetTextViewsAndFields() {
        sumOfWorkedHoursTextView.setText("0");
        workHistoryTextView.setText("");
        sumOfWorkedHours = 0;
    }

    private void clearFiles() throws IOException {
        clearSumOfWorkedHoursFile();
    }

    private void clearSumOfWorkedHoursFile() throws IOException {
        FileOutputStream outputStream = openFileOutput("sum_of_worked_hours.txt", Context.MODE_PRIVATE);
        outputStream.write("0".getBytes());
        outputStream.close();
    }

    public void displaySalary(View view) {
        makeText(CONTEXT, formatDouble(getSalaryAsString()), LENGTH_LONG).show();
    }

    private String getSalaryAsString() {
        return Double.toString((sumOfWorkedHours * SALARY_PER_HOUR));
    }
}
