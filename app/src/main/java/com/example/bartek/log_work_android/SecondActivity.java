package com.example.bartek.log_work_android;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        String sumOfWorkedHoursString = getSumOfWorkedHoursFromExtras();
        sumOfWorkedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        sumOfWorkedHoursTextView.setText(sumOfWorkedHoursString);
        sumOfWorkedHours = parseDouble(sumOfWorkedHoursString);
        workHistoryTextView = (TextView) findViewById(R.id.workHistory);

        workHistoryTextView.setText(getWorkHistory());
    }

    @NonNull
    private String getWorkHistory() {
        try {
            return getWorkHistoryFromFile();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }

    @NonNull
    private String getWorkHistoryFromFile() throws IOException {
        FileInputStream fileInputStream = CONTEXT.openFileInput("work_history.txt");
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder workHistory = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            workHistory.append("\n");
            workHistory.append(line);
        }
        fileInputStream.close();
        reader.close();
        bufferedReader.close();
        return workHistory.toString();
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
        clearWorkHistoryFile();
    }

    private void clearWorkHistoryFile() throws IOException {
        FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
        outputStream.write("".getBytes());
        outputStream.close();
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
